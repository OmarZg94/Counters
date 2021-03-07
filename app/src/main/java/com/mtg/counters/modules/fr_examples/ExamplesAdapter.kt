package com.mtg.counters.modules.fr_examples

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mtg.counters.R
import com.mtg.counters.databinding.ItemExamplesBinding
import com.mtg.counters.utils.BindingViewHolder

class ExamplesAdapter(private var list: MutableList<String>, private var listener: ExampleClickListener) : RecyclerView.Adapter<BindingViewHolder<ItemExamplesBinding>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ItemExamplesBinding> {
        return BindingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_examples, parent, false))
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ItemExamplesBinding>, position: Int) {
        holder.getBinding().txtExampleItem.text = list[position]
        holder.getBinding().txtExampleItem.setOnClickListener { listener.onExampleClick(list[position]) }
    }

    override fun getItemCount(): Int = list.size
}
