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
import co.omisego.omisego.model.TransactionConsumption
import co.omisego.omisego.model.TransactionConsumptionStatus
import co.omisego.omisego.model.params.admin.TransactionConsumptionParams
import co.omisego.omisego.operation.startListeningEvents
import co.omisego.omisego.websocket.listener.TransactionConsumptionListener
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.AmountFormat
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.network.ClientProvider
import network.omisego.omgmerchant.pages.authorized.confirm.ConfirmFragmentArgs
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository

class HandlerConsumeTransactionRequest(
    private val localRepository: LocalRepository,
    override val remoteRepository: RemoteRepository
) : AbstractConfirmHandler() {
    override lateinit var args: ConfirmFragmentArgs
    override lateinit var liveDirection: MutableLiveData<Event<NavDirections>>
    lateinit var liveTransactionConsumptionCancelId: MutableLiveData<String>
    private val socketClient by lazy {
        ClientProvider.createSocketClient(localRepository.loadCredential())
    }

    /**
     * Handle raw payload by consume the transaction request
     */
    override fun onHandlePayload(payload: String) {
        val params = createTransactionConsumptionParams(payload)
        remoteRepository.consumeTransactionRequest(params).enqueue(object : OMGCallback<TransactionConsumption> {
            override fun fail(response: OMGResponse<APIError>) {
                handleFailToHandlePayload(response.data)
            }

            override fun success(response: OMGResponse<TransactionConsumption>) {
                val data = response.data
                if (data.transactionRequest.requireConfirmation) {
                    liveTransactionConsumptionCancelId.value = data.id
                    data.stopListening(socketClient)
                    data.startListeningEvents(socketClient, listener = object : TransactionConsumptionListener() {
                        override fun onTransactionConsumptionFinalizedFail(transactionConsumption: TransactionConsumption, apiError: APIError) {
                            data.stopListening(socketClient)
                            handleFailToHandlePayload(apiError)
                        }

                        override fun onTransactionConsumptionFinalizedSuccess(transactionConsumption: TransactionConsumption) {
                            data.stopListening(socketClient)
                            handleSucceedToHandlePayload(transactionConsumption)
                        }
                    })
                } else {
                    handleSucceedToHandlePayload(data)
                }
            }
        })
    }

    override fun <T> handleSucceedToHandlePayload(data: T) {
        if (data is TransactionConsumption) {
            when (data.status) {
                TransactionConsumptionStatus.CONFIRMED -> {
                    val feedback = Feedback.success(
                        args.transactionType,
                        data
                    )
                    liveDirection.value = Event(createDestinationFeedback(feedback))
                }
                else -> {
                }
            }
        }
    }

    override fun handleFailToHandlePayload(error: APIError) {
        val feedback = Feedback.error(args, null, args.user, error)
        liveDirection.value = Event(createDestinationFeedback(feedback))
    }

    internal fun createTransactionConsumptionParams(payload: String): TransactionConsumptionParams {
        return paramsCreator.createTransactionConsumptionParams(
            formattedId = payload,
            formattedAmount = AmountFormat.Unit(args.amount.toBigDecimal(), args.token.subunitToUnit),
            tokenId = args.token.id,
            accountId = localRepository.loadAccount()?.id,
            exchangeAccountId = localRepository.loadAccount()?.id,
            exchangeWalletAddress = localRepository.loadWallet()?.address
        )
    }
}
