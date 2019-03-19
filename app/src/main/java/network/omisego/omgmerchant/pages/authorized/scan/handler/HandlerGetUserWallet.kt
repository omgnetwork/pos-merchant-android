package network.omisego.omgmerchant.pages.authorized.scan.handler

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 10/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import co.omisego.omisego.custom.OMGCallback
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.Wallet
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.network.ParamsCreator
import network.omisego.omgmerchant.pages.authorized.scan.ScanFragmentArgs
import network.omisego.omgmerchant.repository.RemoteRepository

class HandlerGetUserWallet(
    val remoteRepository: RemoteRepository,
    override val paramsCreator: ParamsCreator = ParamsCreator()
) : AbstractScanHandler {
    override lateinit var args: ScanFragmentArgs
    override lateinit var liveDirection: MutableLiveData<Event<NavDirections>>

    override fun retrieve(payload: String) {
        remoteRepository.loadWallet(paramsCreator.createLoadWalletParams(payload)).enqueue(object : OMGCallback<Wallet> {
            override fun fail(response: OMGResponse<APIError>) {
                handleFailToRetrieveUserInformation(response.data)
            }

            override fun success(response: OMGResponse<Wallet>) {
                handleSucceedToRetrieveUserInformation(response.data)
            }
        })
    }

    override fun <R> handleSucceedToRetrieveUserInformation(data: R) {
        if (data is Wallet) else {
            throw UnsupportedOperationException("Need to handle.")
        }
    }

    override fun handleFailToRetrieveUserInformation(error: APIError) {
        val feedback = Feedback.error(args, null, null, error)
        liveDirection.value = Event(createActionForFeedbackPage(feedback))
    }
}