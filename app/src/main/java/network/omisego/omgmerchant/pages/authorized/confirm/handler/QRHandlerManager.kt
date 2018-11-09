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
import co.omisego.omisego.model.TransactionRequest
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.operation.startListeningEvents
import co.omisego.omisego.websocket.listener.TransactionConsumptionListener
import network.omisego.omgmerchant.extensions.observeEventFor
import network.omisego.omgmerchant.extensions.provideMainFragmentAndroidViewModel
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
            val transactionRequestIds = rawPayload.split("|")
            payload = if (args.transactionType == SCAN_TOPUP) {
                transactionRequestIds[0]
            } else {
                transactionRequestIds[1]
            }
            viewModelProvider.provideMainFragmentAndroidViewModel<ConsumeTransactionRequestHandlerViewModel>().apply {
                this.liveFeedback = this@QRHandlerManager.liveFeedback
            }
        } else {
            // should be the user_address
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

    internal fun AbstractQRHandler.observeEvent(payload: String) {
        viewModelProvider.observeEventFor(liveAPIResult) { result ->
            when (result) {
                is APIResult.Success<*> -> {
                    if (result.data is Transaction) {
                        val feedback = handleSucceedToHandlePayload(result)
                        dispatchSuccessEvent(feedback)
                    } else if (result.data is TransactionConsumption) {
                        if (result.data.transactionRequest.requireConfirmation) {
                            result.data.stopListening(socketClient)
                            result.data.startListeningEvents(socketClient, listener = object : TransactionConsumptionListener() {
                                override fun onTransactionConsumptionFinalizedFail(transactionConsumption: TransactionConsumption, apiError: APIError) {
                                    result.data.stopListening(socketClient)
                                    liveLoading.value = Event(false)
                                    handleFailToHandlePayload(transactionConsumption.transactionRequest.formattedId, apiError)
                                }

                                override fun onTransactionConsumptionFinalizedSuccess(transactionConsumption: TransactionConsumption) {
                                    result.data.stopListening(socketClient)
                                    val feedback = handleSucceedToHandlePayload(APIResult.Success(transactionConsumption))
                                    dispatchSuccessEvent(feedback)
                                }
                            })
                        } else {
                            val transaction = handleSucceedToHandlePayload(result)
                            dispatchSuccessEvent(transaction)
                        }
                    }
                }
                is APIResult.Fail<*> -> {
                    liveLoading.value = Event(false)
                    handleFailToHandlePayload(payload, result.error as APIError)
                }
            }
        }

        /* Listen for failed cases */
        when (this) {
            is CreateTransactionHandlerViewModel -> {
                viewModelProvider.observeEventFor(liveUserInformation) {
                    it.handle<Wallet>(
                        this::handleSucceedToRetrieveUserInformation,
                        this::handleFailToRetrieveUserInformation
                    )
                }
            }
            is ConsumeTransactionRequestHandlerViewModel -> {
                viewModelProvider.observeEventFor(liveUserInformation) {
                    it.handle<TransactionRequest>(
                        this::handleSucceedToRetrieveUserInformation,
                        this::handleFailToRetrieveUserInformation
                    )
                }
            }
            else -> {
                throw UnsupportedOperationException("Need implementation.")
            }
        }
    }

    internal fun dispatchSuccessEvent(feedback: Feedback) {
        liveLoading.value = Event(false)
        liveFeedback.value = feedback
    }
}
