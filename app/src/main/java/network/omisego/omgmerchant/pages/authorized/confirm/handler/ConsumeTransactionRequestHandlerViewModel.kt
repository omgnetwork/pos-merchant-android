package network.omisego.omgmerchant.pages.authorized.confirm.handler

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.Transaction
import co.omisego.omisego.model.TransactionConsumption
import co.omisego.omisego.model.params.admin.TransactionConsumptionParams
import network.omisego.omgmerchant.data.RemoteRepository
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.authorized.confirm.ConfirmFragmentArgs

class ConsumeTransactionRequestHandlerViewModel(
    override val remoteRepository: RemoteRepository
) : ViewModel(), AbstractQRHandler {
    override lateinit var args: ConfirmFragmentArgs
    override var liveFeedback: MutableLiveData<Feedback>? = null
    override val liveAPIResult: MutableLiveData<Event<APIResult>> by lazy { MutableLiveData<Event<APIResult>>() }

    override fun onHandlePayload(payload: String) {
        val params = TransactionConsumptionParams.create(payload)
        remoteRepository.consumeTransactionRequest(params, liveAPIResult)
    }

    override fun <T> convertResultToTransaction(success: APIResult.Success<T>): Transaction {
        val data = success.data as TransactionConsumption
        return data.transaction!!
    }
}
