package network.omisego.omgmerchant.pages.authorized.scan.handler

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 10/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.TransactionRequest
import network.omisego.omgmerchant.NavBottomNavigationDirections
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.network.ParamsCreator
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_TOPUP
import network.omisego.omgmerchant.pages.authorized.scan.ScanFragmentArgs

interface AbstractScanHandler {
    val paramsCreator: ParamsCreator
    var args: ScanFragmentArgs
    var liveDirection: MutableLiveData<Event<NavDirections>>

    fun retrieve(payload: String)

    fun <R> handleSucceedToRetrieveUserInformation(data: R)

    fun handleFailToRetrieveUserInformation(error: APIError)

    fun createActionForFeedbackPage(feedback: Feedback): NavBottomNavigationDirections.ActionGlobalFeedbackFragment {
        return NavBottomNavigationDirections.ActionGlobalFeedbackFragment(feedback)
    }

    fun createActionForConfirmPage(address: String, transactionRequest: TransactionRequest?): NavDirections {
        return when (args.transactionType) {
            SCAN_RECEIVE -> {
                NavBottomNavigationDirections.ActionGlobalConfirmFragment(args.token, transactionRequest, address)
                    .setAmount(args.amount)
                    .setTransactionType(SCAN_RECEIVE)
            }
            SCAN_TOPUP -> {
                NavBottomNavigationDirections.ActionGlobalConfirmFragment(args.token, transactionRequest, address)
                    .setAmount(args.amount)
                    .setTransactionType(SCAN_TOPUP)
            }
            else -> {
                throw UnsupportedOperationException("Need handle.")
            }
        }
    }
}