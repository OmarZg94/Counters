package com.mtg.counters.modules.fr_main

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
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
        iterator.getLastCounters().observe(viewLifecycleOwner, { counters ->
            binding.lytLoading.visibility = GONE
            binding.txtTotalItems.visibility = VISIBLE
            binding.txtTotalItems.text = if (counters.size > 1) "${counters.size} items" else "${counters.size} item"
            var times = 0L
            counters.forEach { times += it.count }
            binding.txtTotalCount.visibility = VISIBLE
            binding.txtTotalCount.text = if (times > 1L) "$times times" else "$times time"
            adapter = CounterAdapter(counters, this)
            binding.rcvCounters.adapter = adapter
        })
        binding.edtSearch.doOnTextChanged { text, _, _, count ->
            if (count == 0) {
                binding.imgClose.visibility = INVISIBLE
            } else {
                binding.imgClose.visibility = VISIBLE
            }
        }
        binding.imgSearchBar.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)
        binding.imgClose.setOnClickListener(this)
        binding.btnAddCounter.setOnClickListener(this)
    }

    override fun onError(counter: Counters, msg: String, option: Int) {
        val alert = AlertDialog.Builder(activity)
        val title = when (option) {
            OPTION_INCREMENT -> String.format(getString(R.string.wt_cant_update), counter.title, (counter.count + 1).toString())
            OPTION_DECREMENT -> String.format(getString(R.string.wt_cant_update), counter.title, (counter.count - 1).toString())
            else -> String.format(getString(R.string.wt_cant_delete), counter.title)
        }
        alert.setTitle(title)
        alert.setMessage(msg)
        alert.setPositiveButton(getString(R.string.t_btn_dismiss)) { _,_ -> binding.lytLoading.visibility = GONE }
        alert.setNegativeButton(getString(R.string.t_btn_retry)) { _, _ ->
            binding.lytLoading.visibility = VISIBLE
            when (option) {
                OPTION_INCREMENT -> iterator.incrementCounter(counter)
                OPTION_DECREMENT -> iterator.decrementCount(counter)
                OPTION_DELETE -> iterator.deleteCounter(counter)
            }
        }
        alert.setCancelable(false)
        alert.show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.imgSearchBar.id -> {
                binding.lytSearchBar.visibility = VISIBLE
                binding.viewShadow.visibility = VISIBLE
                binding.imgSearchBar.visibility = GONE
                binding.edtSearch.isFocusableInTouchMode = true
                binding.edtSearch.requestFocus()
                binding.edtSearch.showKeyboard()
            }
            binding.imgClose.id -> {
                binding.edtSearch.setText("")
                binding.edtSearch.showKeyboard()
            }
            binding.imgBack.id -> {
                binding.lytSearchBar.visibility = GONE
                binding.viewShadow.visibility = GONE
                binding.imgSearchBar.visibility = VISIBLE
                binding.edtSearch.setText("")
                binding.edtSearch.hideKeyboard()
            }
            binding.btnAddCounter.id -> findNavController().navigate(R.id.present_create_item)
        }
    }

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

    override fun onSubtractCounter(counter: Counters) {
        if (!Application.getContext().isNetworkEnable()) {
            val alert = AlertDialog.Builder(activity)
            alert.setTitle(getString(R.string.wt_internet_problem))
            alert.setMessage(getString(R.string.ws_offline_device))
            alert.setPositiveButton(getString(R.string.t_btn_ok), null)
            alert.show()
        } else {
            binding.lytLoading.visibility = VISIBLE
        }
    }

    override fun onLongClickCounter(counter: Counters) {
        if (!Application.getContext().isNetworkEnable()) {
            val alert = AlertDialog.Builder(activity)
            alert.setTitle(getString(R.string.wt_internet_problem))
            alert.setMessage(getString(R.string.ws_offline_device))
            alert.setPositiveButton(getString(R.string.t_btn_ok), null)
            alert.show()
        } else {
            binding.lytLoading.visibility = VISIBLE
        }
    }

    companion object {
        const val OPTION_INCREMENT = 1
        const val OPTION_DECREMENT = 2
        const val OPTION_DELETE = 3

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
    fun onError(counter: Counters, msg: String, option: Int)
}

interface CounterClickListener {
    fun onAddCounter(counter: Counters)
    fun onSubtractCounter(counter: Counters)
    fun onLongClickCounter(counter: Counters)
}