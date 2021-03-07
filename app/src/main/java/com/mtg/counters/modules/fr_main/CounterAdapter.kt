package com.mtg.counters.modules.fr_main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mtg.counters.Application
import com.mtg.counters.R
import com.mtg.counters.databinding.ItemCounterBinding
import com.mtg.counters.db.entities.Counters
import com.mtg.counters.utils.BindingViewHolder

class CounterAdapter(private var list: MutableList<Counters>, private var listener: CounterClickListener) : RecyclerView.Adapter<BindingViewHolder<ItemCounterBinding>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ItemCounterBinding> {
        return BindingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_counter, parent, false))
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ItemCounterBinding>, position: Int) {
        holder.getBinding().txtCounterTitle.text = list[position].title
        holder.getBinding().txtCounterValue.text = "${list[position].count}"
        holder.getBinding().imgPlus.setOnClickListener { listener.onAddCounter(list[position]) }
        holder.getBinding().clyItemCounter.setOnLongClickListener {
            listener.onLongClickCounter(list[position])
            true
        }
        /* If Counter Value Is 0 Then Color The View As Enable Item */
        if (list[position].count == 0L) {
            holder.getBinding().imgMinus.setOnClickListener(null)
            holder.getBinding().imgMinus.setColorFilter(ContextCompat.getColor(Application.getContext(), R.color.secondary))
            holder.getBinding().txtCounterValue.setTextColor(ContextCompat.getColor(Application.getContext(), R.color.secondary))
        } else {
            holder.getBinding().imgMinus.setOnClickListener { listener.onSubtractCounter(list[position]) }
            holder.getBinding().imgMinus.setColorFilter(ContextCompat.getColor(Application.getContext(), R.color.app_tint))
            holder.getBinding().txtCounterValue.setTextColor(ContextCompat.getColor(Application.getContext(), R.color.black))
        }
    }

    override fun getItemCount(): Int = list.size
}
