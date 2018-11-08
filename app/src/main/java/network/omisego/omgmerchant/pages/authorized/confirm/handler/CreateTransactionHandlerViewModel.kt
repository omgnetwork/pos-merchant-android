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
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository
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
    override val liveUserInformation: MutableLiveData<Event<APIResult>> by lazy { MutableLiveData<Event<APIResult>>() }

    override fun onHandlePayload(payload: String) {
        remoteRepository.transfer(createTransactionCreateParams(payload), liveAPIResult)
    }

    override fun <T> handleSucceedToHandlePayload(success: APIResult.Success<T>): Feedback {
        return Feedback.success(args.transactionType, success.data as Transaction)
    }

    override fun handleFailToHandlePayload(payload: String, error: APIError) {
        this.error = error
        remoteRepository.loadWallet(WalletParams(payload), liveUserInformation)
    }

    override fun <R> handleSucceedToRetrieveUserInformation(data: R) {
        if (data is Wallet) {
            liveFeedback?.value = Feedback.error(args, data.address, data.user, error)
            return
        }

        throw IllegalStateException("Expected object ${Wallet::class}, but got unexpected object : $data")
    }

    override fun handleFailToRetrieveUserInformation(error: APIError) {
        liveFeedback?.value = Feedback.error(args, null, null, error)
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
}
