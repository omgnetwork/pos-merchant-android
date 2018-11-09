package network.omisego.omgmerchant.custom

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

class CustomLoadingRecyclerAdapter<T : Any, V : ViewDataBinding>(
    @LayoutRes private val loadingRes: Int,
    @LayoutRes private val contentRes: Int,
    private val stateViewHolderBinding: CustomStateViewHolderBinding<T, V>
) : RecyclerView.Adapter<CustomStateViewHolder>() {
    private val contentLoadingList: MutableList<Any> = mutableListOf()
    private val contentList: MutableList<T> = mutableListOf()

    fun addItems(newContentItems: List<T>) {
        contentList.addAll(newContentItems)
        dispatchUpdate(contentLoadingList, contentList)
        contentLoadingList.clear()
        contentLoadingList.addAll(contentList)
    }

    fun reloadItems(newContentItems: List<T>) {
        contentList.clear()
        contentList.addAll(newContentItems)
        dispatchUpdate(contentLoadingList, contentList)
        contentLoadingList.clear()
        contentLoadingList.addAll(contentList)
    }

    fun addLoadingItems(totalLoadingItems: Int) {
        (0 until totalLoadingItems).forEach { _ ->
            contentLoadingList.add(UUID.randomUUID())
        }
        dispatchUpdate(contentList, contentLoadingList)
    }

    fun clearItems() {
        contentLoadingList.clear()
        dispatchUpdate(contentList, contentLoadingList)
        contentList.clear()
    }

    private fun dispatchUpdate(oldList: List<Any>, newList: List<Any>) {
        val diff = CustomLoadingDiffCallback(oldList, newList)
        val result = DiffUtil.calculateDiff(diff)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomStateViewHolder {
        val layoutRes = when (viewType) {
            1 -> loadingRes
            2 -> contentRes
            else -> throw UnsupportedOperationException("Currently not support viewType $viewType")
        }

        return when (viewType) {
            1 -> {
                val itemView = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
                CustomStateViewHolder.Loading(itemView)
            }
            2 -> {
                val binding: V = DataBindingUtil.inflate(LayoutInflater.from(parent.context), contentRes, parent, false)
                CustomStateViewHolder.Show(binding)
            }
            else -> {
                throw UnsupportedOperationException("Currently not support viewType $viewType")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (contentLoadingList[position] is UUID) {
            1
        } else {
            2
        }
    }

    override fun getItemCount() = contentLoadingList.size

    override fun onBindViewHolder(holder: CustomStateViewHolder, position: Int) {
        when (holder) {
            is CustomStateViewHolder.Show<*> -> {
                stateViewHolderBinding.bind((holder as CustomStateViewHolder.Show<V>).binding, contentList[position])
            }
        }
    }
}
