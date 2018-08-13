package network.omisego.omgmerchant.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

sealed class StateRecyclerAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    abstract val items: List<T>

    class Loading<T>(
        override val items: List<T> = listOf(),
        @LayoutRes private val layout: Int
    ) : StateRecyclerAdapter<T>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val rootView = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            return object : RecyclerView.ViewHolder(rootView) {}
        }

        override fun getItemCount(): Int = 5
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
    }

    class Show<T, R : ViewDataBinding>(
        override val items: List<T>,
        @LayoutRes private val layout: Int,
        private val viewHolderBinding: StateViewHolderBinding<T, R>
    ) : StateRecyclerAdapter<T>() {
        private lateinit var binding: R

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layout,
                parent,
                false
            )
            return object : RecyclerView.ViewHolder(binding.root) {}
        }

        override fun getItemCount() = items.size
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            viewHolderBinding.bind(binding, items[position])
        }
    }
}
