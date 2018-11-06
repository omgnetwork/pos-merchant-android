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

class QRHandlerManager(
    private val viewModelProvider: Fragment
) {
    lateinit var liveLoading: MutableLiveData<Event<Boolean>>
    lateinit var liveTransaction: MutableLiveData<Event<Transaction>>
    lateinit var liveAPIError: MutableLiveData<Event<APIError>>
    lateinit var liveFeedback: MutableLiveData<Feedback>
    private val socketClient by lazy {
        ClientProvider.socketClient
    }

    companion object {
        const val PREFIX_TX_REQUEST = "txr_"
    }

    fun handleQRPayload(payload: String, args: ConfirmFragmentArgs) {
        val handler: AbstractQRHandler = if (payload.startsWith(PREFIX_TX_REQUEST)) {
            // the payload should be the transaction_request_id
            viewModelProvider.provideMainFragmentViewModel<ConsumeTransactionRequestHandlerViewModel>()
        } else {
            // the payload should be the user_address
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
                        val transaction = convertResultToTransaction(result)
                        dispatchSuccessEvent(transaction)
                    } else if (result.data is TransactionConsumption) {
                        result.data.stopListening(socketClient)
                        result.data.startListeningEvents(socketClient, listener = object : TransactionConsumptionListener() {
                            override fun onTransactionConsumptionFinalizedFail(transactionConsumption: TransactionConsumption, apiError: APIError) {
                                result.data.stopListening(socketClient)
                                dispatchErrorEvent(apiError)
                            }

                            override fun onTransactionConsumptionFinalizedSuccess(transactionConsumption: TransactionConsumption) {
                                result.data.stopListening(socketClient)
                                val transaction = convertResultToTransaction(result)
                                dispatchSuccessEvent(transaction)
                            }
                        })
                    }
                }
                is APIResult.Fail<*> -> {
                    val error = result.error as APIError
                    dispatchErrorEvent(error)
                }
            }
        }

        if (this is CreateTransactionHandlerViewModel) {
            viewModelProvider.observeEventFor(liveTransaction) { transaction ->
                handleTransferSuccess(args, transaction)
            }
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

    internal fun dispatchSuccessEvent(transaction: Transaction) {
        liveLoading.value = Event(false)
        liveTransaction.value = Event(transaction)
    }

    internal fun dispatchErrorEvent(error: APIError) {
        liveLoading.value = Event(false)
        liveAPIError.value = Event(error)
    }
}
