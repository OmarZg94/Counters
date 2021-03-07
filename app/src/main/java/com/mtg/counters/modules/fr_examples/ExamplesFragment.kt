package com.mtg.counters.modules.fr_examples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mtg.counters.Application
import com.mtg.counters.R
import com.mtg.counters.databinding.FragmentExamplesBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ExamplesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExamplesFragment : Fragment(), ExampleClickListener {

    private lateinit var binding: FragmentExamplesBinding
    private val iterator by lazy { ExamplesIterator() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_examples, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.rcvDrinks.layoutManager = LinearLayoutManager(Application.getContext(), RecyclerView.HORIZONTAL, false)
        binding.rcvFood.layoutManager = LinearLayoutManager(Application.getContext(), RecyclerView.HORIZONTAL, false)
        binding.rcvMisc.layoutManager = LinearLayoutManager(Application.getContext(), RecyclerView.HORIZONTAL, false)
        binding.rcvDrinks.adapter = iterator.getDrinksAdapter(this)
        binding.rcvFood.adapter = iterator.getFoodAdapter(this)
        binding.rcvMisc.adapter = iterator.getMiscAdapter(this)
    }

    override fun onExampleClick(name: String) {
        iterator.saveTemporalExample(name)
        findNavController().popBackStack()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ExamplesFragment.
         */
        @JvmStatic
        fun newInstance() =
                ExamplesFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}

interface ExampleClickListener {
    fun onExampleClick(name: String)
}

