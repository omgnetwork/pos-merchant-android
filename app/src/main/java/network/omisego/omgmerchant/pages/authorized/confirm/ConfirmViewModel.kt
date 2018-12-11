package network.omisego.omgmerchant.pages.authorized.confirm

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.AmountFormat
import network.omisego.omgmerchant.pages.authorized.confirm.handler.AbstractConfirmHandler
import network.omisego.omgmerchant.pages.authorized.confirm.handler.HandlerConsumeTransactionRequest
import network.omisego.omgmerchant.pages.authorized.confirm.handler.HandlerCreateTransaction
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository

class ConfirmViewModel(
    val app: Application,
    val localRepository: LocalRepository,
    val remoteRepository: RemoteRepository
) : AndroidViewModel(app) {
    var handler: AbstractConfirmHandler? = null
    lateinit var args: ConfirmFragmentArgs
    lateinit var liveDirection: MutableLiveData<Event<NavDirections>>
    lateinit var liveCancelTransactionConsumptionId: MutableLiveData<String>
    val liveYesClick: MutableLiveData<Event<View>> by lazy { MutableLiveData<Event<View>>() }
    val liveNoClick: MutableLiveData<Event<View>> by lazy { MutableLiveData<Event<View>>() }

    val email: String
        get() = args.user.email

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

    companion object {
        const val PREFIX_TX_REQUEST = "txr_"
    }

    fun handleYesClick(view: View) {
        liveYesClick.value = Event(view)
    }

    fun handleNoClick(view: View) {
        liveNoClick.value = Event(view)
    }

    fun handleQRPayload() {
        handler = findConfirmHandler()
        liveDirection.value = Event(handler!!.createDestinationLoading())
        handler!!.args = args
        handler!!.onHandlePayload(args.address)
    }

    internal fun findConfirmHandler(): AbstractConfirmHandler {
        return if (args.address.startsWith(PREFIX_TX_REQUEST)) {
            HandlerConsumeTransactionRequest(localRepository, remoteRepository).apply {
                this.liveDirection = this@ConfirmViewModel.liveDirection
                this.liveTransactionConsumptionCancelId = this@ConfirmViewModel.liveCancelTransactionConsumptionId
            }
        } else {
            HandlerCreateTransaction(localRepository, remoteRepository).apply {
                this.liveDirection = this@ConfirmViewModel.liveDirection
            }
        }
    }
}
