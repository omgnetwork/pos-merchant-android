package network.omisego.omgmerchant.pages.authorized.confirm.handler

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Transaction
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.model.params.WalletParams
import co.omisego.omisego.model.params.admin.TransactionCreateParams
import network.omisego.omgmerchant.data.LocalRepository
import network.omisego.omgmerchant.data.RemoteRepository
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.authorized.confirm.ConfirmFragmentArgs
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE

class CreateTransactionHandlerViewModel(
    val localRepository: LocalRepository,
    override val remoteRepository: RemoteRepository
) : ViewModel(), AbstractQRHandler {
    override lateinit var args: ConfirmFragmentArgs
    override var liveFeedback: MutableLiveData<Feedback>? = null
    var error: APIError? = null

    override val liveAPIResult: MutableLiveData<Event<APIResult>> by lazy { MutableLiveData<Event<APIResult>>() }
    val liveWallet: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }

    override fun onHandlePayload(payload: String) {
        remoteRepository.transfer(createTransactionCreateParams(payload), liveAPIResult)
    }

    override fun <T> convertResultToTransaction(success: APIResult.Success<T>): Transaction {
        return success.data as Transaction
    }

    internal fun createTransactionCreateParams(payload: String): TransactionCreateParams {
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

    fun getUserWallet(address: String) {
        remoteRepository.loadWallet(WalletParams(address), liveWallet)
    }

    fun handleTransferSuccess(args: ConfirmFragmentArgs, transaction: Transaction) {
        liveFeedback?.value = Feedback.success(args.transactionType, transaction)
    }

    fun handleTransferFail(qrPayload: String, error: APIError) {
        getUserWallet(qrPayload)
        this.error = error
    }

    fun handleGetWalletSuccess(wallet: Wallet) {
        liveFeedback?.value = Feedback.error(args, wallet, error)
    }

    fun handleGetWalletFailed(error: APIError) {
//        toast(error.description)
    }
}
