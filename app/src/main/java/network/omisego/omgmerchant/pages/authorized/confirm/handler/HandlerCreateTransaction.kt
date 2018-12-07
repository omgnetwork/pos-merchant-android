package network.omisego.omgmerchant.pages.authorized.confirm.handler

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import co.omisego.omisego.custom.OMGCallback
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.Transaction
import co.omisego.omisego.model.TransactionConsumption
import co.omisego.omisego.model.params.admin.TransactionCreateParams
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.AmountFormat
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.authorized.confirm.ConfirmFragmentArgs
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository

class HandlerCreateTransaction(
    val localRepository: LocalRepository,
    override val remoteRepository: RemoteRepository
) : AbstractConfirmHandler() {
    override lateinit var liveDirection: MutableLiveData<Event<NavDirections>>
    override lateinit var args: ConfirmFragmentArgs

    override fun onHandlePayload(payload: String) {
        remoteRepository.transfer(createTransactionCreateParams(payload)).enqueue(object : OMGCallback<Transaction> {
            override fun fail(response: OMGResponse<APIError>) {
                handleFailToHandlePayload(response.data)
            }

            override fun success(response: OMGResponse<Transaction>) {
                handleSucceedToHandlePayload(response.data)
            }
        })
    }

    override fun <T> handleSucceedToHandlePayload(data: T) {
        if (data is Transaction) {
            val feedback = Feedback.success(args.transactionType, data)
            liveDirection.value = Event(createDestinationFeedback(feedback))
        }
    }

    override fun handleFailToHandlePayload(error: APIError) {
        val feedback = Feedback.error(args, null, args.user, error)
        liveDirection.value = Event(createDestinationFeedback(feedback))
    }

    internal fun createTransactionCreateParams(payload: String): TransactionCreateParams {
        when (args.transactionType) {
            SCAN_RECEIVE -> {
                return paramsCreator.createTransactionCreateParams(
                    payload,
                    localRepository.loadWallet()?.address!!,
                    AmountFormat.Unit(args.amount.toBigDecimal(), args.token.subunitToUnit),
                    args.token.id
                )
            }
            else -> {
                return paramsCreator.createTransactionCreateParams(
                    localRepository.loadWallet()?.address!!,
                    payload,
                    AmountFormat.Unit(args.amount.toBigDecimal(), args.token.subunitToUnit),
                    args.token.id
                )
            }
        }
    }

    override fun stopListening() {
        // no-op
    }
}
