package network.omisego.omgmerchant.pages.account

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.support.v7.widget.RecyclerView
import android.view.View
import co.omisego.omisego.model.Account
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.viewholder_account.view.*

class SelectAccountViewHolder(
    private val rootView: View
) : RecyclerView.ViewHolder(rootView) {

    fun bind(account: Account) {
        Glide.with(rootView.imageView.context).load("https://api.adorable.io/avatars/214/${account.name}.png").into(rootView.imageView)
        rootView.tvAccountName.text = account.name
    }
}