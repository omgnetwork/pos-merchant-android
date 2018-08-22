package network.omisego.omgmerchant.base

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.UUID

class LoadingRecyclerAdapter<T, V : ViewDataBinding>(
    @LayoutRes private val loadingRes: Int,
    @LayoutRes private val contentRes: Int,
    private val stateViewHolderBinding: StateViewHolderBinding<T, V>
) : RecyclerView.Adapter<StateViewHolder>() {
    private val contentItems: MutableList<T> = mutableListOf()
    private val loadingItems: MutableList<String> = mutableListOf()

    fun addItems(newContentItems: List<T>) {
        contentItems.addAll(newContentItems)
        dispatchUpdate()
        loadingItems.clear()
    }

    fun addLoadingItems(totalLoadingItems: Int) {
        for (item in 0 until totalLoadingItems) {
            loadingItems.add(UUID.randomUUID().toString())
        }
        dispatchUpdate()
    }

    fun clearItems() {
        contentItems.clear()
        loadingItems.clear()
    }

    private fun dispatchUpdate() {
        val diff = LoadingDiffCallback(loadingItems, contentItems)
        val result = DiffUtil.calculateDiff(diff)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateViewHolder {
        val layoutRes = when (viewType) {
            1 -> loadingRes
            2 -> contentRes
            else -> throw UnsupportedOperationException("Currently not support viewType $viewType")
        }

        return when (viewType) {
            1 -> {
                val itemView = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
                StateViewHolder.Loading(itemView)
            }
            2 -> {
                val binding: V = DataBindingUtil.inflate(LayoutInflater.from(parent.context), contentRes, parent, false)
                StateViewHolder.Show(binding)
            }
            else -> {
                throw UnsupportedOperationException("Currently not support viewType $viewType")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position >= contentItems.size) {
            1
        } else {
            2
        }
    }

    override fun getItemCount() = loadingItems.size + contentItems.size

    override fun onBindViewHolder(holder: StateViewHolder, position: Int) {
        when (holder) {
            is StateViewHolder.Show<*> -> {
                stateViewHolderBinding.bind((holder as StateViewHolder.Show<V>).binding, contentItems[position])
            }
        }
    }
}
