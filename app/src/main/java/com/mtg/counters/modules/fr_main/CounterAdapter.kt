package com.mtg.counters.modules.fr_main

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mtg.counters.Application
import com.mtg.counters.R
import com.mtg.counters.databinding.ItemCounterBinding
import com.mtg.counters.db.entities.Counters
import com.mtg.counters.utils.BindingViewHolder

class CounterAdapter(private var list: MutableList<Counters>, private var listener: CounterClickListener) : RecyclerView.Adapter<BindingViewHolder<ItemCounterBinding>>(),
        Filterable {

    private var filteredList: MutableList<Counters>
    private var isEditing = false
    private var isFiltering = false

    init {
        setHasStableIds(true)
        filteredList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ItemCounterBinding> {
        return BindingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_counter, parent, false))
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ItemCounterBinding>, position: Int) {
        holder.getBinding().txtCounterTitle.text = filteredList[position].title
        holder.getBinding().txtCounterValue.text = "${filteredList[position].count}"
        holder.getBinding().imgPlus.setOnClickListener { listener.onAddCounter(filteredList[position]) }
        /* If Counter Value Is 0 Then Color The View As Enable Item */
        if (filteredList[position].count == 0L) {
            holder.getBinding().imgMinus.setOnClickListener(null)
            holder.getBinding().imgMinus.setColorFilter(ContextCompat.getColor(Application.getContext(), R.color.secondary))
            holder.getBinding().txtCounterValue.setTextColor(ContextCompat.getColor(Application.getContext(), R.color.secondary))
        } else {
            holder.getBinding().imgMinus.setOnClickListener { listener.onSubtractCounter(filteredList[position]) }
            holder.getBinding().imgMinus.setColorFilter(ContextCompat.getColor(Application.getContext(), R.color.app_tint))
            holder.getBinding().txtCounterValue.setTextColor(ContextCompat.getColor(Application.getContext(), R.color.black))
        }
        /* Change Item UI If It's On Editing Mode */
        if (!isEditing) {
            holder.getBinding().imgMinus.visibility = VISIBLE
            holder.getBinding().imgPlus.visibility = VISIBLE
            holder.getBinding().txtCounterValue.visibility = VISIBLE
            holder.getBinding().imgCheck.visibility = GONE
            holder.getBinding().clyItemCounter.setBackgroundColor(Color.TRANSPARENT)
            /* Manage Editing Callback */
            if (!isFiltering) {
                holder.getBinding().clyItemCounter.setOnLongClickListener {
                    isEditing = true
                    filteredList[position].isSelected = true
                    listener.onEditingCounters()
                    notifyDataSetChanged()
                    true
                }
            } else {
                holder.getBinding().clyItemCounter.setOnLongClickListener(null)
            }
        } else {
            holder.getBinding().imgMinus.visibility = GONE
            holder.getBinding().imgPlus.visibility = GONE
            holder.getBinding().txtCounterValue.visibility = GONE
            if (filteredList[position].isSelected) {
                holder.getBinding().clyItemCounter.setBackgroundColor(ContextCompat.getColor(Application.getContext(), R.color.app_tint_transparent))
                holder.getBinding().imgCheck.visibility = VISIBLE
            } else {
                holder.getBinding().clyItemCounter.setBackgroundColor(Color.TRANSPARENT)
                holder.getBinding().imgCheck.visibility = GONE
            }
            /* Manage Editing Callback */
            holder.getBinding().clyItemCounter.setOnClickListener {
                filteredList[position].isSelected = !filteredList[position].isSelected
                listener.onSelectCounter()
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = filteredList.size

    fun getFilteredList(): MutableList<Counters> = filteredList

    fun resetEditing() {
        isEditing = false
        filteredList.forEach { it.isSelected = false }
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                filteredList = mutableListOf()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(list)
                } else {
                    val pattern = constraint.toString().toLowerCase().trim()
                    for (item in list) {
                        if (item.title.toLowerCase().contains(pattern)) {
                            filteredList.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                isFiltering = !(constraint == null || constraint.isEmpty())
                notifyDataSetChanged()
                listener.onFilterComplete()
            }
        }
    }
}
