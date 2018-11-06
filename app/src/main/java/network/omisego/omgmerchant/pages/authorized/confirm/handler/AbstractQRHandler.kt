package network.omisego.omgmerchant.pages.authorized.confirm.handler

import android.arch.lifecycle.MutableLiveData
import co.omisego.omisego.model.Transaction
import network.omisego.omgmerchant.data.RemoteRepository
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.authorized.confirm.ConfirmFragmentArgs

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

/**
 * For simple transfer we need the `user_address`
 * For consume the transaction request we need the `transaction_request_id`.
 *
 * For simple transfer we will call `createTransaction` to transfer.
 * For consume the transaction request we will need to call `createTransactionConsumption` to consume.
 *
 * After finish calling to the api, for the simple transfer we just move to `Feedback` page
 * After finish calling to the api, for the consumption of transaction request, we need to go to `WaitingConfirmationFragment`, waiting for finalized event, then go to the `Feedback` page.
 *
 */
interface AbstractQRHandler {
    var args: ConfirmFragmentArgs

    val remoteRepository: RemoteRepository

    val liveAPIResult: MutableLiveData<Event<APIResult>>
    var liveFeedback: MutableLiveData<Feedback>?

    /**
     * @param payload the param that need to be used for request to the appropriate APIs such as `createTransaction` or `consumeTransactionRequest`
     */
    fun onHandlePayload(payload: String)

    fun <T> convertResultToTransaction(success: APIResult.Success<T>): Transaction
}
