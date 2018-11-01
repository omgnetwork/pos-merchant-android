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
import network.omisego.omgmerchant.data.LocalRepository
import network.omisego.omgmerchant.data.RemoteRepository
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import java.util.Date

class ConfirmViewModel(
    val app: Application,
    val localRepository: LocalRepository,
    val remoteRepository: RemoteRepository
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
        remoteRepository.loadWallet(WalletParams(address), liveWallet)
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
                    toAddress = localRepository.loadWallet()?.address!!,
                    amount = args.amount.toBigDecimal().multiply(args.token.subunitToUnit).setScale(0),
                    tokenId = args.token.id
                )
            }
            else -> {
                return TransactionCreateParams(
                    fromAddress = localRepository.loadWallet()?.address,
                    toAddress = payload,
                    amount = args.amount.toBigDecimal().multiply(args.token.subunitToUnit).setScale(0),
                    tokenId = args.token.id
                )
            }
        }
    }

    fun createFeedback(transaction: Transaction): Feedback {
        return createFeedback(args.transactionType, transaction)
    }

    fun createFeedback(wallet: Wallet): Feedback {
        return createFeedback(
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

    private fun createFeedback(transactionType: String, source: TransactionSource, error: APIError? = null): Feedback {
        return if (transactionType.equals(SCAN_RECEIVE, true)) Feedback(
            false,
            transactionType,
            Date(),
            source,
            error
        ) else {
            Feedback(
                false,
                transactionType,
                Date(),
                source,
                error
            )
        }
    }

    fun createFeedback(transactionType: String, transaction: Transaction): Feedback {
        return if (transactionType.equals(SCAN_RECEIVE, true)) Feedback(
            true,
            transactionType,
            transaction.createdAt,
            transaction.from
        ) else {
            Feedback(
                true,
                transactionType,
                transaction.createdAt,
                transaction.to
            )
        }
    }

    fun transfer(params: TransactionCreateParams) = remoteRepository.transfer(params, liveTransaction)
}
