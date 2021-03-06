package com.mtg.counters.modules.fr_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mtg.counters.Application
import com.mtg.counters.R
import com.mtg.counters.databinding.FragmentMainBinding
import com.mtg.counters.extensions.hideKeyboard
import com.mtg.counters.extensions.showKeyboard

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.rcvCounters.layoutManager = LinearLayoutManager(Application.getContext(), RecyclerView.VERTICAL, false)
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

    companion object {
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