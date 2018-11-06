package network.omisego.omgmerchant.pages.authorized.confirm.handler

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.support.v4.app.Fragment
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Transaction
import co.omisego.omisego.model.TransactionConsumption
import co.omisego.omisego.operation.startListeningEvents
import co.omisego.omisego.websocket.listener.TransactionConsumptionListener
import network.omisego.omgmerchant.extensions.observeEventFor
import network.omisego.omgmerchant.extensions.observeFor
import network.omisego.omgmerchant.extensions.provideMainFragmentViewModel
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.network.ClientProvider
import network.omisego.omgmerchant.pages.authorized.confirm.ConfirmFragmentArgs
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_TOPUP

class QRHandlerManager(
    private val viewModelProvider: Fragment
) {
    lateinit var liveLoading: MutableLiveData<Event<Boolean>>
    lateinit var liveAPIError: MutableLiveData<Event<APIError>>
    lateinit var liveFeedback: MutableLiveData<Feedback>
    private val socketClient by lazy {
        ClientProvider.socketClient
    }

    companion object {
        const val PREFIX_TX_REQUEST = "txr_"
    }

    fun handleQRPayload(rawPayload: String, args: ConfirmFragmentArgs) {
        val payload: String
        val handler: AbstractQRHandler = if (rawPayload.startsWith(PREFIX_TX_REQUEST)) {
            // the rawPayload should be the transaction_request_id
            // receive, send -> top-up, receive
            val transactionRequestIds = rawPayload.split("|")
            payload = if (args.transactionType == SCAN_TOPUP) {
                transactionRequestIds[0]
            } else {
                transactionRequestIds[1]
            }
            viewModelProvider.provideMainFragmentViewModel<ConsumeTransactionRequestHandlerViewModel>()
        } else {
            // the rawPayload should be the user_address
            payload = rawPayload
            viewModelProvider.provideMainFragmentViewModel<CreateTransactionHandlerViewModel>().apply {
                this.liveFeedback = this@QRHandlerManager.liveFeedback
            }
        }

        liveLoading.value = Event(true)
        handler.args = args
        handler.observeEvent(payload)
        handler.onHandlePayload(payload)
    }

    fun AbstractQRHandler.observeEvent(payload: String) {
        viewModelProvider.observeEventFor(liveAPIResult) { result ->
            when (result) {
                is APIResult.Success<*> -> {
                    if (result.data is Transaction) {
                        val transaction = convertResultToFeedback(result)
                        dispatchSuccessEvent(transaction)
                    } else if (result.data is TransactionConsumption) {
                        result.data.stopListening(socketClient)
                        if (result.data.transactionRequest.requireConfirmation) {
                            result.data.startListeningEvents(socketClient, listener = object : TransactionConsumptionListener() {
                                override fun onTransactionConsumptionFinalizedFail(transactionConsumption: TransactionConsumption, apiError: APIError) {
                                    result.data.stopListening(socketClient)
                                    dispatchErrorEvent(apiError)
                                }

                                override fun onTransactionConsumptionFinalizedSuccess(transactionConsumption: TransactionConsumption) {
                                    result.data.stopListening(socketClient)
                                    val transaction = convertResultToFeedback(result)
                                    dispatchSuccessEvent(transaction)
                                }
                            })
                        } else {
                            val transaction = convertResultToFeedback(result)
                            dispatchSuccessEvent(transaction)
                        }
                    }
                }
                is APIResult.Fail<*> -> {
                    val error = result.error as APIError
                    dispatchErrorEvent(error)
                }
            }
        }

        if (this is CreateTransactionHandlerViewModel) {
            viewModelProvider.observeEventFor(liveAPIError) { error ->
                handleTransferFail(payload, error)
            }
            viewModelProvider.observeFor(liveWallet) {
                it.handle(
                    this::handleGetWalletSuccess,
                    this::handleGetWalletFailed
                )
            }
        } else if (this is ConsumeTransactionRequestHandlerViewModel) {

        }
    }

    internal fun dispatchSuccessEvent(feedback: Feedback) {
        liveLoading.value = Event(false)
        liveFeedback.value = feedback
    }

    internal fun dispatchErrorEvent(error: APIError) {
        liveLoading.value = Event(false)
        liveAPIError.value = Event(error)
    }
}
