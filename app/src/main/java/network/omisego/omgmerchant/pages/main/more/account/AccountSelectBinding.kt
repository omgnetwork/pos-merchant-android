package network.omisego.omgmerchant.pages.main.more.account

import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import android.view.View
import android.widget.TextView
import co.omisego.omisego.model.Account

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object AccountSelectBinding {
    @JvmStatic
    @BindingAdapter("liveAccount", "account")
    fun setCheckmarkVisible(view: TextView, liveAccount: MutableLiveData<Account>, account: Account) {
        liveAccount.observeForever {
            when(it?.id == account.id){
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.GONE
            }
        }
    }
}