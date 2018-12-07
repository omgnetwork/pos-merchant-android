package network.omisego.omgmerchant.pages.authorized.loading

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 14/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import co.omisego.omisego.constant.enums.ErrorCode
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.TransactionConsumption
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.extensions.logi
import network.omisego.omgmerchant.extensions.map
import network.omisego.omgmerchant.helper.HelperContext
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.network.ParamsCreator
import network.omisego.omgmerchant.pages.authorized.LoadingNavDirectionCreator
import network.omisego.omgmerchant.pages.authorized.NavDirectionCreator
import network.omisego.omgmerchant.pages.authorized.confirm.ConfirmFragmentArgs
import network.omisego.omgmerchant.repository.RemoteRepository

class LoadingViewModel(
    private val remoteRepository: RemoteRepository,
    private val paramsCreator: ParamsCreator = ParamsCreator(),
    private val navDirectionCreator: LoadingNavDirectionCreator = NavDirectionCreator()
) : ViewModel(), LoadingNavDirectionCreator by navDirectionCreator {
    val liveTransactionConsumptionCancelId: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val liveTransactionConsumptionRejectResult: MutableLiveData<Event<APIResult>> by lazy { MutableLiveData<Event<APIResult>>() }
    val liveShowingCancelButton: LiveData<Boolean> by lazy { liveTransactionConsumptionCancelId.map { !it.isNullOrEmpty() } }
    lateinit var liveCancelTransactionConsumption: MutableLiveData<Boolean>
    lateinit var liveDirection: MutableLiveData<Event<NavDirections>>
    var confirmFragmentArgs: ConfirmFragmentArgs? = null

    fun cancelTransactionConsumption() {
        val params = paramsCreator.createTransactionConsumptionActionParams(liveTransactionConsumptionCancelId.value!!)
        remoteRepository.rejectTransactionConsumption(params, liveTransactionConsumptionRejectResult)
        liveCancelTransactionConsumption.value = true
    }

    fun handleRejectTransactionConsumptionSuccess(transactionConsumption: TransactionConsumption) {
        logi("Canceled the transaction consumption ${transactionConsumption.id}")
        val args = confirmFragmentArgs ?: return
        val feedback = Feedback.error(
            args,
            transactionConsumption.transactionRequest.address,
            transactionConsumption.transactionRequest.user,
            APIError(ErrorCode.SDK_UNEXPECTED_ERROR, HelperContext.context.getString(R.string.feedback_canceled))
        )
        liveDirection.value = Event(createDestinationFeedback(feedback))
        liveCancelTransactionConsumption.value = false
    }

    fun handleRejectTransactionConsumptionFailed(error: APIError) {
        logi("Cannot cancel the transaction request :${error.description}")
        val args = confirmFragmentArgs ?: return
        val feedback = Feedback.error(args, null, args.user, error)
        liveDirection.value = Event(createDestinationFeedback(feedback))
        liveCancelTransactionConsumption.value = false
    }
}
