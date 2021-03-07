package com.mtg.counters.modules.fr_main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mtg.counters.Application
import com.mtg.counters.R
import com.mtg.counters.databinding.FragmentMainBinding
import com.mtg.counters.db.entities.Counters
import com.mtg.counters.extensions.hideKeyboard
import com.mtg.counters.extensions.isNetworkEnable
import com.mtg.counters.extensions.showKeyboard

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment(), MainPresenter, OnClickListener, CounterClickListener {

    private lateinit var binding: FragmentMainBinding
    private lateinit var iterator: MainIterator
    private lateinit var adapter: CounterAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        iterator = ViewModelProvider(this).get(MainIterator::class.java)
        iterator.setListener(this)
        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.rcvCounters.layoutManager = LinearLayoutManager(Application.getContext(), RecyclerView.VERTICAL, false)
        binding.toolbarMain.imgBack.setOnClickListener(this)
        binding.toolbarMain.imgClose.setOnClickListener(this)
        binding.btnAddCounter.setOnClickListener(this)
        /* Download All Counters From Server */
        if (!Application.getContext().isNetworkEnable()) {
            val alert = AlertDialog.Builder(activity)
            alert.setTitle(getString(R.string.wt_internet_problem))
            alert.setMessage(getString(R.string.ws_offline_device))
            alert.setPositiveButton(getString(R.string.t_btn_dismiss), null)
            alert.setNegativeButton(getString(R.string.t_btn_retry)) { _, _ ->
                binding.lytLoading.visibility = VISIBLE
                iterator.downloadAllCounters()
            }
            alert.show()
        } else {
            binding.lytLoading.visibility = VISIBLE
            iterator.downloadAllCounters()
        }
        binding.swpCounters.setColorSchemeResources(R.color.app_tint)
        binding.swpCounters.setProgressBackgroundColorSchemeResource(R.color.white)
    }

    override fun onResume() {
        super.onResume()
        if (binding.toolbarMain.imgSearchBar.isVisible) binding.viewShadowSearch.visibility = GONE
    }

    override fun onError(counter: Counters?, msg: String, option: Int) {
        val alert = AlertDialog.Builder(activity)
        val title = when (option) {
            OPTION_INCREMENT -> String.format(getString(R.string.wt_cant_update), counter!!.title, (counter.count + 1).toString())
            OPTION_DECREMENT -> String.format(getString(R.string.wt_cant_update), counter!!.title, (counter.count - 1).toString())
            OPTION_DELETE -> String.format(getString(R.string.wt_cant_delete), counter!!.title)
            else -> getString(R.string.wt_cant_download)
        }
        alert.setTitle(title)
        alert.setMessage(msg)
        alert.setPositiveButton(getString(R.string.t_btn_dismiss)) { _, _ -> if (option != OPTION_GET_ALL) binding.lytLoading.visibility = GONE }
        alert.setNegativeButton(getString(R.string.t_btn_retry)) { _, _ ->
            binding.lytLoading.visibility = VISIBLE
            when (option) {
                OPTION_INCREMENT -> iterator.incrementCounter(counter!!)
                OPTION_DECREMENT -> iterator.decrementCount(counter!!)
                OPTION_DELETE -> iterator.deleteCounters(mutableListOf(counter!!))
                OPTION_GET_ALL -> iterator.downloadAllCounters()
            }
        }
        alert.setCancelable(false)
        alert.show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.toolbarMain.imgSearchBar.id -> {
                binding.toolbarMain.lytSearchBar.visibility = VISIBLE
                binding.viewShadow.visibility = VISIBLE
                binding.toolbarMain.imgSearchBar.visibility = GONE
                binding.toolbarMain.edtSearch.isFocusableInTouchMode = true
                binding.toolbarMain.edtSearch.isFocusable = true
                binding.toolbarMain.edtSearch.requestFocus()
                binding.toolbarMain.edtSearch.showKeyboard()
                binding.toolbarMain.edtSearch.setText("")
            }
            binding.toolbarMain.imgClose.id -> {
                binding.toolbarMain.edtSearch.setText("")
                binding.toolbarMain.edtSearch.showKeyboard()
            }
            binding.toolbarMain.imgBack.id -> {
                binding.toolbarMain.lytSearchBar.visibility = GONE
                binding.viewShadow.visibility = GONE
                binding.viewShadowSearch.visibility = GONE
                binding.toolbarMain.imgSearchBar.visibility = VISIBLE
                adapter.filter.filter("")
                binding.toolbarMain.edtSearch.hideKeyboard()
            }
            binding.toolbarMain.imgCloseShare.id -> {
                binding.toolbarMain.lytShareBar.visibility = GONE
                binding.viewShadow.visibility = GONE
                binding.toolbarMain.imgSearchBar.visibility = VISIBLE
                adapter.resetEditing()
            }
            binding.toolbarMain.imgDelete.id -> {
                var title = "Delete "
                val listToDelete = mutableListOf<Counters>()
                adapter.getFilteredList().forEach {
                    if (it.isSelected) {
                        listToDelete.add(it)
                        title += "\"${it.title}\""
                    }
                }
                val alert = AlertDialog.Builder(activity)
                alert.setTitle("$title?")
                alert.setPositiveButton(getString(R.string.t_btn_delete)) { _, _ ->
                    binding.lytLoading.visibility = VISIBLE
                    iterator.deleteCounters(listToDelete)
                }
                alert.setNegativeButton(getString(R.string.t_btn_cancel), null)
                alert.show()
            }
            binding.toolbarMain.imgShare.id -> {
                var textToShare = ""
                adapter.getFilteredList().forEach {
                    if (it.isSelected) textToShare += "${it.count} × ${it.title} - "
                }
                val intent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, textToShare.substring(0, textToShare.length-3))
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(intent, null)
                startActivity(shareIntent)
            }
            binding.btnAddCounter.id -> findNavController().navigate(R.id.present_create_item)
        }
    }

    /* Callback To Manage The Visibility And Refresh Of Counters List To The UI */
    override fun showLiveCounters() {
        iterator.getLastCounters().observe(viewLifecycleOwner, { counters ->
            binding.swpCounters.isRefreshing = false
            adapter = CounterAdapter(counters, this)
            binding.rcvCounters.adapter = adapter
            if (counters.size > 0) {
                binding.lytLoading.visibility = GONE
                binding.pgbSearchCounters.visibility = VISIBLE
                binding.txtNoCounters.visibility = GONE
                binding.txtPhrase.visibility = GONE
                binding.txtTotalItems.visibility = VISIBLE
                binding.txtTotalItems.text = if (counters.size > 1) "${counters.size} items" else "${counters.size} item"
                var times = 0L
                counters.forEach { times += it.count }
                binding.txtTotalCount.visibility = VISIBLE
                binding.txtTotalCount.text = if (times > 1L) "$times times" else "$times time"
                binding.toolbarMain.imgSearchBar.setOnClickListener(this)
                binding.swpCounters.isEnabled = true
                binding.swpCounters.setOnRefreshListener {
                    binding.swpCounters.isRefreshing = true
                    iterator.downloadAllCounters()
                }
            } else {
                binding.pgbSearchCounters.visibility = GONE
                binding.txtNoCounters.visibility = VISIBLE
                binding.txtPhrase.visibility = VISIBLE
                binding.txtTotalItems.visibility = GONE
                binding.txtTotalCount.visibility = GONE
                binding.toolbarMain.imgSearchBar.setOnClickListener(null)
                binding.swpCounters.isEnabled = false
            }
        })
        binding.toolbarMain.edtSearch.doOnTextChanged { text, _, _, count ->
            adapter.filter.filter(text)
            if (count == 0) {
                binding.toolbarMain.imgClose.visibility = INVISIBLE
                binding.viewShadowSearch.visibility = VISIBLE
            } else {
                binding.toolbarMain.imgClose.visibility = VISIBLE
                binding.viewShadowSearch.visibility = GONE
            }
        }
    }

    /* Callback To Manage When User Click The Add Button Of The List */
    override fun onAddCounter(counter: Counters) {
        if (!Application.getContext().isNetworkEnable()) {
            val alert = AlertDialog.Builder(activity)
            alert.setTitle(getString(R.string.wt_internet_problem))
            alert.setMessage(getString(R.string.ws_offline_device))
            alert.setPositiveButton(getString(R.string.t_btn_ok), null)
            alert.show()
        } else {
            binding.lytLoading.visibility = VISIBLE
            iterator.incrementCounter(counter)
        }
    }

    /* Callback To Manage When User Click The Subtraction Button Of The List */
    override fun onSubtractCounter(counter: Counters) {
        if (!Application.getContext().isNetworkEnable()) {
            val alert = AlertDialog.Builder(activity)
            alert.setTitle(getString(R.string.wt_internet_problem))
            alert.setMessage(getString(R.string.ws_offline_device))
            alert.setPositiveButton(getString(R.string.t_btn_ok), null)
            alert.show()
        } else {
            binding.lytLoading.visibility = VISIBLE
            iterator.decrementCount(counter)
        }
    }

    /* Callback To Manage When User Long Pressed A Counter Item For The First Time */
    override fun onEditingCounters() {
        binding.toolbarMain.imgSearchBar.visibility = GONE
        binding.toolbarMain.lytShareBar.visibility = VISIBLE
        binding.viewShadow.visibility = VISIBLE
        binding.toolbarMain.imgCloseShare.setOnClickListener(this)
        binding.toolbarMain.imgDelete.setOnClickListener(this)
        binding.toolbarMain.imgShare.setOnClickListener(this)
    }

    /* Callback To Manage When User Select A Counter Of Editing List */
    override fun onSelectCounter() {
        var totalItemsSelected = 0
        adapter.getFilteredList().forEach { if (it.isSelected) totalItemsSelected++ }
        if (totalItemsSelected > 0)
            binding.toolbarMain.txtSelected.text = "$totalItemsSelected selected"
        else
            binding.toolbarMain.imgCloseShare.performClick()
    }

    /* Callback To Manage The Publish Results Of The Filtering Action */
    override fun onFilterComplete() {
        if (adapter.itemCount == 0) {
            binding.txtTotalItems.visibility = GONE
            binding.txtTotalCount.visibility = GONE
            binding.txtEmptySearch.visibility = VISIBLE
        } else {
            val filteredList = adapter.getFilteredList()
            binding.txtTotalItems.visibility = VISIBLE
            binding.txtTotalCount.visibility = VISIBLE
            binding.txtEmptySearch.visibility = GONE
            binding.txtTotalItems.text = if (filteredList.size > 1) "${filteredList.size} items" else "${filteredList.size} item"
            var times = 0L
            filteredList.forEach { times += it.count }
            binding.txtTotalCount.text = if (times > 1L) "$times times" else "$times time"
        }
    }

    /* Callback When Delete Counters Of Server Are Finished */
    override fun onDeleteCounterComplete() {
        binding.toolbarMain.imgCloseShare.performClick()
        showLiveCounters()
    }

    companion object {
        const val OPTION_INCREMENT = 1
        const val OPTION_DECREMENT = 2
        const val OPTION_DELETE = 3
        const val OPTION_GET_ALL = 4

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MainFragment.
         */
        @JvmStatic
        fun newInstance() =
                MainFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}

interface MainPresenter {
    fun onError(counter: Counters?, msg: String, option: Int)
    fun showLiveCounters()
    fun onDeleteCounterComplete()
}

interface CounterClickListener {
    fun onAddCounter(counter: Counters)
    fun onSubtractCounter(counter: Counters)
    fun onEditingCounters()
    fun onSelectCounter()
    fun onFilterComplete()
}