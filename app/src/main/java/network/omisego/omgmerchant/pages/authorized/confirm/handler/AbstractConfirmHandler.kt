package network.omisego.omgmerchant.pages.authorized.confirm.handler

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import co.omisego.omisego.model.APIError
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.network.ParamsCreator
import network.omisego.omgmerchant.pages.authorized.ConfirmNavDirectionCreator
import network.omisego.omgmerchant.pages.authorized.NavDirectionCreator
import network.omisego.omgmerchant.pages.authorized.confirm.ConfirmFragmentArgs
import network.omisego.omgmerchant.repository.RemoteRepository

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

/**
 * **** The comment below was provided to remind myself what is going on here. ****
 *
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
abstract class AbstractConfirmHandler(
    private val navDirectionCreator: ConfirmNavDirectionCreator = NavDirectionCreator(),
    val paramsCreator: ParamsCreator = ParamsCreator()
) : ConfirmNavDirectionCreator by navDirectionCreator {
    abstract var args: ConfirmFragmentArgs
    abstract val remoteRepository: RemoteRepository
    abstract val liveDirection: MutableLiveData<Event<NavDirections>>

    abstract fun onHandlePayload(payload: String)

    abstract fun <T> handleSucceedToHandlePayload(data: T)

    abstract fun handleFailToHandlePayload(error: APIError)

    abstract fun stopListening()
}
