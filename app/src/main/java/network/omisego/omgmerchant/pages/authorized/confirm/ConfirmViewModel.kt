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
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.model.params.WalletParams
import co.omisego.omisego.model.transaction.Transaction
import co.omisego.omisego.model.transaction.TransactionSource
import co.omisego.omisego.model.transaction.send.TransactionCreateParams
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE

class ConfirmViewModel(
    val app: Application,
    val repository: ConfirmRepository
) : AndroidViewModel(app) {
    lateinit var args: ConfirmFragmentArgs
    var error: APIError? = null
    lateinit var address: String
    val liveTransaction: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }
    val liveWallet: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }
    val liveYesClick: MutableLiveData<Event<View>> by lazy { MutableLiveData<Event<View>>() }
    val liveNoClick: MutableLiveData<Event<View>> by lazy { MutableLiveData<Event<View>>() }

    val addressText: String
        get() = address

    val amountText: String
        get() = app.getString(R.string.confirm_transaction_amount, args.amount.toBigDecimal(), args.token.symbol)

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

    fun getUserWallet(address: String) {
        repository.getUserWallet(WalletParams(address), liveWallet)
    }

    fun handleYesClick(view: View) {
        liveYesClick.value = Event(view)
    }

    fun handleNoClick(view: View) {
        liveNoClick.value = Event(view)
    }

    fun provideTransactionCreateParams(payload: String): TransactionCreateParams {
        when (args.transactionType) {
            SCAN_RECEIVE -> {
                return TransactionCreateParams(
                    fromAddress = payload,
                    toAddress = repository.loadWallet().address,
                    amount = args.amount.toBigDecimal().multiply(args.token.subunitToUnit).setScale(0),
                    tokenId = args.token.id
                )
            }
            else -> {
                return TransactionCreateParams(
                    fromAddress = repository.loadWallet().address,
                    toAddress = payload,
                    amount = args.amount.toBigDecimal().multiply(args.token.subunitToUnit).setScale(0),
                    tokenId = args.token.id
                )
            }
        }
    }

    fun saveFeedback(transaction: Transaction) {
        repository.saveFeedback(args.transactionType, transaction)
    }

    fun saveFeedback(wallet: Wallet) {
        repository.saveFeedback(
            args.transactionType,
            TransactionSource(
                wallet.address,
                args.amount.toBigDecimal().multiply(args.token.subunitToUnit),
                args.token.id,
                args.token,
                wallet.userId,
                wallet.user,
                null,
                null
            ),
            error)
    }

    fun transfer(params: TransactionCreateParams) = repository.transfer(params, liveTransaction)
}
