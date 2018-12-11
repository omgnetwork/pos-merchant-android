package network.omisego.omgmerchant.custom

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.View

sealed class CustomStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    class Loading(itemView: View) : CustomStateViewHolder(itemView)
    class Show<T : ViewDataBinding>(val binding: T) : CustomStateViewHolder(binding.root)
}
