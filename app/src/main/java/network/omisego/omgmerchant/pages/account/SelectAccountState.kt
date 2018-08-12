package network.omisego.omgmerchant.pages.account

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import co.omisego.omisego.model.Account
import network.omisego.omgmerchant.R

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

sealed class SelectAccountState : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class Loading() : SelectAccountState() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val rootView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_account_loading, parent, false)
            return object : RecyclerView.ViewHolder(rootView) {}
        }

        override fun getItemCount(): Int = 5
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
    }

    class Show(
        private val items: List<Account>
    ) : SelectAccountState() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val rootLayout = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_account, parent, false)
            return SelectAccountViewHolder(rootLayout)
        }

        override fun getItemCount() = items.size
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is SelectAccountViewHolder) {
                holder.bind(items[position])
            }
        }
    }
}
