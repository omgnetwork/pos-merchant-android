package network.omisego.omgmerchant.pages.main.more.transaction

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.databinding.BindingAdapter
import android.support.v4.content.ContextCompat
import android.widget.TextView
import co.omisego.omisego.model.Account
import co.omisego.omisego.model.pagination.Paginable
import co.omisego.omisego.model.transaction.Transaction
import network.omisego.omgmerchant.R

object TransactionBindingAdapter {
    @JvmStatic
    @BindingAdapter("transaction")
    fun colorizedTransaction(tv: TextView, transaction: Transaction) {
        when (transaction.status) {
            Paginable.Transaction.TransactionStatus.CONFIRMED -> {
                tv.setTextColor(ContextCompat.getColor(tv.context, R.color.colorGreen))
            }
            Paginable.Transaction.TransactionStatus.FAILED -> {
                tv.setTextColor(ContextCompat.getColor(tv.context, R.color.colorRed))
            }
            else -> {
                tv.setTextColor(ContextCompat.getColor(tv.context, R.color.colorGrayWeak))
            }
        }
    }

    @JvmStatic
    @BindingAdapter("transaction", "account")
    fun colorizedTransaction(tv: TextView, transaction: Transaction, account: Account) {
        when (transaction.status) {
            Paginable.Transaction.TransactionStatus.CONFIRMED -> {
                if (transaction.to.accountId != null) {
                    tv.setTextColor(ContextCompat.getColor(tv.context, R.color.colorGreen))
                } else {
                    tv.setTextColor(ContextCompat.getColor(tv.context, R.color.colorRed))
                }
            }
            else -> {
                tv.setTextColor(ContextCompat.getColor(tv.context, R.color.colorGray))
            }
        }
    }
}
