package network.omisego.omgmerchant.pages.account

import android.arch.lifecycle.MutableLiveData
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class SelectAccountAdapter(
    private val state: MutableLiveData<SelectAccountState>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return state.value!!.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return state.value!!.itemCount
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return state.value!!.onBindViewHolder(holder, position)
    }

//    init {
//        state.observe(this)
//    }
}
