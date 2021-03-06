package com.mtg.counters.modules.fr_welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mtg.counters.R
import com.mtg.counters.databinding.FragmentWelcomeBinding


/**
 * A simple [Fragment] subclass.
 * Use the [WelcomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_welcome, container, false)
        binding.btnStart.setOnClickListener { findNavController().navigate(R.id.present_main_fragment) }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment WelcomeFragment.
         */
        @JvmStatic
        fun newInstance() =
                WelcomeFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}