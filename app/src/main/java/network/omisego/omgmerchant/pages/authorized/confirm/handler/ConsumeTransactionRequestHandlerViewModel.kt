package network.omisego.omgmerchant.pages.authorized.confirm.handler

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.TransactionConsumption
import co.omisego.omisego.model.params.admin.TransactionConsumptionParams
import network.omisego.omgmerchant.data.LocalRepository
import network.omisego.omgmerchant.data.RemoteRepository
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.authorized.confirm.ConfirmFragmentArgs

class ConsumeTransactionRequestHandlerViewModel(
    private val localRepository: LocalRepository,
    override val remoteRepository: RemoteRepository
) : ViewModel(), AbstractQRHandler {
    override lateinit var args: ConfirmFragmentArgs
    override var liveFeedback: MutableLiveData<Feedback>? = null
    override val liveAPIResult: MutableLiveData<Event<APIResult>> by lazy { MutableLiveData<Event<APIResult>>() }

    override fun onHandlePayload(payload: String) {
        val params = createTransactionConsumptionParams(payload)
        remoteRepository.consumeTransactionRequest(params, liveAPIResult)
    }

    override fun <T> convertResultToFeedback(success: APIResult.Success<T>): Feedback {
        val data = success.data as TransactionConsumption
        return Feedback.success(
            args.transactionType,
            data
        )
    }

    internal fun createTransactionConsumptionParams(payload: String): TransactionConsumptionParams {
        return TransactionConsumptionParams.create(
            formattedTransactionRequestId = payload,
            amount = args.amount.toBigDecimal().multiply(args.token.subunitToUnit).setScale(0),
            tokenId = args.token.id,
            accountId = localRepository.loadAccount()?.id,
            exchangeAccountId = localRepository.loadAccount()?.id,
            exchangeWalletAddress = localRepository.loadWallet()?.address
        )
    }
}
