package network.omisego.omgmerchant.pages.authorized.loading

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 14/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.TransactionConsumption
import network.omisego.omgmerchant.extensions.logi
import network.omisego.omgmerchant.extensions.map
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.network.ParamsCreator
import network.omisego.omgmerchant.repository.RemoteRepository

class LoadingViewModel(
    private val remoteRepository: RemoteRepository,
    private val paramsCreator: ParamsCreator = ParamsCreator()
) : ViewModel() {
    val liveTransactionConsumptionCancelId: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val liveTransactionConsumptionRejectResult: MutableLiveData<Event<APIResult>> by lazy { MutableLiveData<Event<APIResult>>() }
    val liveShowingCancelButton: LiveData<Boolean> by lazy { liveTransactionConsumptionCancelId.map { !it.isNullOrEmpty() } }

    fun cancelTransactionConsumption() {
        val params = paramsCreator.createTransactionConsumptionActionParams(liveTransactionConsumptionCancelId.value!!)
        remoteRepository.rejectTransactionConsumption(params, liveTransactionConsumptionRejectResult)
    }

    fun handleRejectTransactionConsumptionSuccess(transactionConsumption: TransactionConsumption) {
        logi("Cancel the transaction consumption ${transactionConsumption.id}")
    }

    fun handleRejectTransactionConsumptionFailed(error: APIError) {
        logi("Something went wrong :${error.description}")
    }
}
