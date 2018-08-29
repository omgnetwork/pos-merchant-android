package network.omisego.omgmerchant.pages.feedback

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 17/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.graphics.drawable.Drawable
import co.omisego.omisego.model.transaction.Transaction
import co.omisego.omisego.model.transaction.send.TransactionCreateParams
import network.omisego.omgmerchant.extensions.mutableLiveDataOf
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.utils.map

class FeedbackViewModel(
    val app: Application,
    private val repository: FeedbackRepository,
    private val transformer: FeedbackTransformer
) : AndroidViewModel(app) {
    val liveTransaction: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }
    val liveLoading: MutableLiveData<Boolean> by lazy { mutableLiveDataOf(false) }
    val liveFeedback: MutableLiveData<Feedback> = MutableLiveData()

    /* binding data */
    val icon: LiveData<Drawable> = liveFeedback.map { transformer.transformIcon(app, it) }
    val iconText: LiveData<String> = liveFeedback.map(transformer::transformIconText)
    val title: LiveData<String> = liveFeedback.map { transformer.transformTitle(app, it) }
    val amount: LiveData<String> = liveFeedback.map { transformer.transformAmount(app, it) }
    val userId: LiveData<String> = liveFeedback.map { transformer.transformUserId(app, it) }
    val userName: LiveData<String> = liveFeedback.map { transformer.transformUserName(app, it) }
    val date: LiveData<String> = liveFeedback.map { transformer.transformDate(app, it) }

    fun transfer() {
        val feedback = liveFeedback.value!!
        val params = when (feedback.transactionType) {
            SCAN_RECEIVE -> {
                TransactionCreateParams(
                    fromAddress = feedback.source.address,
                    toAddress = repository.loadWallet()!!.address,
                    amount = feedback.source.amount.setScale(0),
                    tokenId = feedback.source.token.id
                )
            }
            else -> {
                TransactionCreateParams(
                    fromAddress = repository.loadWallet()!!.address,
                    toAddress = feedback.source.address,
                    amount = feedback.source.amount.setScale(0),
                    tokenId = feedback.source.token.id
                )
            }
        }
        repository.transfer(params, liveTransaction)
        liveLoading.value = true
    }

    fun setFeedback(transactionType: String, transaction: Transaction) {
        liveFeedback.value = if (transactionType.equals(SCAN_RECEIVE, true)) Feedback(
                true,
                transactionType,
                transaction.createdAt,
                transaction.from
            ) else {
                Feedback(
                    true,
                    transactionType,
                    transaction.createdAt,
                    transaction.to
                )
            }
    }

    fun deletePersistenceFeedback() {
        repository.deleteFeedback()
    }
}
