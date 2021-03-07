package com.mtg.counters.utils

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

class BindingViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: T = DataBindingUtil.bind(itemView)!!

    fun getBinding(): T = binding
}