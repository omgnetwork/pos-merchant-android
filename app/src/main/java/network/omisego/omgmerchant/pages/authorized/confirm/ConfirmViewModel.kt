package network.omisego.omgmerchant.pages.authorized.confirm

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.view.View
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.AmountFormat
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository

class ConfirmViewModel(
    val app: Application,
    val localRepository: LocalRepository,
    val remoteRepository: RemoteRepository
) : AndroidViewModel(app) {
    lateinit var args: ConfirmFragmentArgs
    lateinit var qrPayload: String
    val liveYesClick: MutableLiveData<Event<View>> by lazy { MutableLiveData<Event<View>>() }
    val liveNoClick: MutableLiveData<Event<View>> by lazy { MutableLiveData<Event<View>>() }

    val addressText: String
        get() = qrPayload

    val amountText: String
        get() {
            val subunit = AmountFormat.Unit(args.amount.toBigDecimal(), args.token.subunitToUnit)
            return String.format(
                "%s %s",
                subunit.display(),
                args.token.symbol
            )
        }

    val transactionDirectionText: String
        get() = if (args.transactionType == SCAN_RECEIVE) {
            app.getString(R.string.confirm_transaction_direction_from)
        } else {
            app.getString(R.string.confirm_transaction_direction_to)
        }

    val transactionTypeText: String
        get() = if (args.transactionType == SCAN_RECEIVE) {
            app.getString(R.string.confirm_transaction_type_receive)
        } else {
            app.getString(R.string.confirm_transaction_type_top_up)
        }

    fun handleYesClick(view: View) {
        liveYesClick.value = Event(view)
    }

    fun handleNoClick(view: View) {
        liveNoClick.value = Event(view)
    }
}
