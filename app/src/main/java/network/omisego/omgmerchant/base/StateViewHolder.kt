package network.omisego.omgmerchant.base

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View

sealed class StateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    class Loading(itemView: View) : StateViewHolder(itemView)
    class Show<T : ViewDataBinding>(val binding: T) : StateViewHolder(binding.root)
}
