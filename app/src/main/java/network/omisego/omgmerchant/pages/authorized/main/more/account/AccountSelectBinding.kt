package network.omisego.omgmerchant.pages.authorized.main.more.account

import androidx.lifecycle.MutableLiveData
import androidx.databinding.BindingAdapter
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.TextView
import co.omisego.omisego.model.Account
import network.omisego.omgmerchant.R

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object AccountSelectBinding {
    @JvmStatic
    @BindingAdapter("liveAccount", "account", "savedAccount")
    fun setCheckmarkVisible(view: TextView, liveAccount: MutableLiveData<Account>, account: Account, savedAccount: Account) {
        liveAccount.observeForever {
            when (it?.id == account.id) {
                true -> {
                    view.visibility = View.VISIBLE
                    view.setTextColor(ContextCompat.getColor(view.context, R.color.colorBlue))
                }
                false -> {
                    if (savedAccount.id == account.id) {
                        view.visibility = View.VISIBLE
                        view.setTextColor(ContextCompat.getColor(view.context, R.color.colorGrayWeak))
                    } else {
                        view.visibility = View.GONE
                    }
                }
            }
        }
    }
}