package network.omisego.omgmerchant.pages.authorized.scan.handler

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 10/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import co.omisego.omisego.custom.OMGCallback
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.TransactionRequest
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.network.ParamsCreator
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_TOPUP
import network.omisego.omgmerchant.pages.authorized.scan.ScanFragmentArgs
import network.omisego.omgmerchant.repository.RemoteRepository

class HandlerGetTransactionRequest(
    val remoteRepository: RemoteRepository,
    override val paramsCreator: ParamsCreator = ParamsCreator()
) : ViewModel(), AbstractScanHandler {
    override lateinit var args: ScanFragmentArgs
    override lateinit var liveDirection: MutableLiveData<Event<NavDirections>>

    override fun retrieve(payload: String) {
        val transactionRequestId = if (payload.contains("|")) {
            val transactionRequestIds = payload.split("|")
            if (args.transactionType == SCAN_TOPUP) {
                transactionRequestIds[0]
            } else {
                transactionRequestIds[1]
            }
        } else {
            payload
        }
        val params = paramsCreator.createGetTransactionRequestParams(transactionRequestId)
        val request = remoteRepository.loadTransactionRequest(params)
        request.enqueue(object : OMGCallback<TransactionRequest> {
            override fun fail(response: OMGResponse<APIError>) {
                handleFailToRetrieveUserInformation(response.data)
            }

            override fun success(response: OMGResponse<TransactionRequest>) {
                handleSucceedToRetrieveUserInformation(response.data)
            }
        })
    }

    override fun <R> handleSucceedToRetrieveUserInformation(data: R) {
        if (data is TransactionRequest) {
            liveDirection.value = Event(createActionForConfirmPage(data.formattedId, data))
        } else {
            throw UnsupportedOperationException("Need to handle.")
        }
    }

    override fun handleFailToRetrieveUserInformation(error: APIError) {
        val feedback = Feedback.error(args, null, null, error)
        liveDirection.value = Event(createActionForFeedbackPage(feedback))
    }
}
