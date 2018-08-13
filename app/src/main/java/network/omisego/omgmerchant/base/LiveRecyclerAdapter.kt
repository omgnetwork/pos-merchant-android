package network.omisego.omgmerchant.base

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

class LiveRecyclerAdapter<T>(
    private val state: LiveData<StateRecyclerAdapter<T>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), LifecycleOwner {
    private val lifecycleRegistry: LifecycleRegistry by lazy { LifecycleRegistry(this) }
    private var lastState: StateRecyclerAdapter<T> = state.value!!

    init {
        lifecycleRegistry.markState(Lifecycle.State.INITIALIZED)
        state.observe(this, Observer {
            when (it!!) {
                is StateRecyclerAdapter.Show<T, *> -> {
                    if (lastState is StateRecyclerAdapter.Loading)
                        notifyItemRangeRemoved(0, lastState.itemCount)
                    notifyItemRangeInserted(0, it.itemCount)
                }
                is StateRecyclerAdapter.Loading<T> -> {
//                    notifyItemRangeInserted(0, it.itemCount)
                }
            }
            lastState = it
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = state.value!!.onCreateViewHolder(parent, viewType)
    override fun getItemCount() = state.value!!.itemCount
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        state.value!!.onBindViewHolder(holder, position)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        lifecycleRegistry.markState(Lifecycle.State.CREATED)
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
        lifecycleRegistry.markState(Lifecycle.State.RESUMED)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
    }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry
}
