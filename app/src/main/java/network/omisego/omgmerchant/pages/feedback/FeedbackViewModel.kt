package network.omisego.omgmerchant.pages.feedback

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 17/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.scan.SCAN_RECEIVE

class FeedbackViewModel(
    val app: Application,
    private val repository: FeedbackRepository
) : AndroidViewModel(app) {
    lateinit var feedback: Feedback
    val title: String
        get() {
            return if (feedback.transactionType.equals(SCAN_RECEIVE, true)) {
                app.getString(R.string.feedback_payment_title)
            } else {
                app.getString(R.string.feedback_topup_title)
            }
        }
    val amount: String
        get() = app.getString(
            R.string.feedback_amount,
            feedback.source.amount.divide(feedback.source.token.subunitToUnit),
            feedback.source.token.symbol
        )
    val userId: String
        get() = app.getString(R.string.feedback_customer_id, feedback.source.userId)
    val userName: String
        get() = app.getString(R.string.feedback_customer_name, "${feedback.source.user?.email ?: feedback.source.user?.username}")
    val date: String
        get() = app.getString(R.string.feedback_date_time, feedback.createdAt)

    fun deleteFeedback() {
        repository.deleteFeedback()
    }
}
