package network.omisego.omgmerchant.pages.authorized.confirm.handler

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import co.omisego.omisego.constant.enums.ErrorCode
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.TransactionConsumption
import co.omisego.omisego.model.TransactionConsumptionStatus
import co.omisego.omisego.model.TransactionRequest
import co.omisego.omisego.model.params.TransactionRequestParams
import co.omisego.omisego.model.params.admin.TransactionConsumptionParams
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.AmountFormat
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.authorized.confirm.ConfirmFragmentArgs
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository

class ConsumeTransactionRequestHandlerViewModel(
    val app: Application,
    private val localRepository: LocalRepository,
    override val remoteRepository: RemoteRepository
) : AndroidViewModel(app), AbstractQRHandler {
    var error: APIError? = null
    override lateinit var args: ConfirmFragmentArgs
    override var liveFeedback: MutableLiveData<Feedback>? = null
    override val liveAPIResult: MutableLiveData<Event<APIResult>> by lazy { MutableLiveData<Event<APIResult>>() }
    override val liveUserInformation: MutableLiveData<Event<APIResult>> by lazy { MutableLiveData<Event<APIResult>>() }

    /**
     * Handle raw payload by consume the transaction request
     */
    override fun onHandlePayload(payload: String) {
        val params = createTransactionConsumptionParams(payload)
        remoteRepository.consumeTransactionRequest(params, liveAPIResult)
    }

    /**
     * When the consumption was successful, then create the feedback object to display
     */
    override fun <T> handleSucceedToHandlePayload(success: APIResult.Success<T>): Feedback {
        val data = success.data as TransactionConsumption
        when (data.status) {
            TransactionConsumptionStatus.CONFIRMED -> {
                return Feedback.success(
                    args.transactionType,
                    data
                )
            }
            TransactionConsumptionStatus.REJECTED -> {
                return Feedback.error(
                    args,
                    data.transactionRequest.address,
                    data.transactionRequest.user,
                    APIError(ErrorCode.SDK_UNEXPECTED_ERROR, app.getString(R.string.feedback_user_reject))
                )
            }
            else -> {
                throw UnsupportedOperationException("Need to handle.")
            }
        }
    }

    override fun handleFailToHandlePayload(payload: String, error: APIError) {
        this.error = error
        remoteRepository.loadTransactionRequest(TransactionRequestParams(payload), liveUserInformation)
    }

    override fun <R> handleSucceedToRetrieveUserInformation(data: R) {
        if (data is TransactionRequest) {
            liveFeedback?.value = Feedback.error(args, data.address, data.user, error)
            return
        }

        throw IllegalStateException("Expected object ${TransactionRequest::class}, but got unexpected object : $data")
    }

    override fun handleFailToRetrieveUserInformation(error: APIError) {
        liveFeedback?.value = Feedback.error(args, null, null, error)
    }

    internal fun createTransactionConsumptionParams(payload: String): TransactionConsumptionParams {
        return TransactionConsumptionParams.create(
            formattedTransactionRequestId = payload,
            amount = AmountFormat.Unit(args.amount.toBigDecimal(), args.token.subunitToUnit).toSubunit().amount,
            tokenId = args.token.id,
            accountId = localRepository.loadAccount()?.id,
            exchangeAccountId = localRepository.loadAccount()?.id,
            exchangeWalletAddress = localRepository.loadWallet()?.address
        )
    }
}
