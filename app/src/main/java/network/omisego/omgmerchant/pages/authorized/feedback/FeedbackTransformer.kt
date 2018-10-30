package network.omisego.omgmerchant.pages.authorized.feedback

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 29/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_TOPUP

class FeedbackTransformer {
    fun transformIcon(context: Context, feedback: Feedback): Drawable {
        return when (feedback.success) {
            true -> ContextCompat.getDrawable(context, R.drawable.shape_circle_solid_blue)!!
            false -> ContextCompat.getDrawable(context, R.drawable.shape_circle_solid_red)!!
        }
    }

    fun transformIconText(feedback: Feedback): String {
        return when (feedback.success) {
            true -> "\uE906"
            false -> "\uE90B"
        }
    }

    fun transformTitle(context: Context, feedback: Feedback): String {
        return when {
            feedback.success && feedback.transactionType == SCAN_RECEIVE -> context.getString(R.string.feedback_success_payment_title)
            feedback.success && feedback.transactionType == SCAN_TOPUP -> context.getString(R.string.feedback_success_topup_title)
            !feedback.success && feedback.transactionType == SCAN_RECEIVE -> context.getString(R.string.feedback_fail_payment_title)
            !feedback.success && feedback.transactionType == SCAN_TOPUP -> context.getString(R.string.feedback_fail_topup_title)
            else -> throw UnsupportedOperationException("This liveFeedback isn't currently supported.")
        }
    }

    fun transformAmount(context: Context, feedback: Feedback): String {
        return context.getString(
            R.string.feedback_amount,
            feedback.source.amount.divide(feedback.source.token.subunitToUnit),
            feedback.source.token.symbol)
    }

    fun transformUserId(context: Context, feedback: Feedback): String {
        return context.getString(R.string.feedback_customer_id, feedback.source.userId)
    }

    fun transformUserName(context: Context, feedback: Feedback): String {
        return context.getString(R.string.feedback_customer_name, "${feedback.source.user?.email
            ?: feedback.source.user?.username}")
    }

    fun transformDate(context: Context, feedback: Feedback): String {
        return context.getString(R.string.feedback_date_time, feedback.createdAt)
    }

    fun transformErrorCode(context: Context, feedback: Feedback): String {
        return "Error code: ${feedback.error?.code?.name?.toUpperCase() ?: "Unknown"}"
    }

    fun transformErrorDescription(context: Context, feedback: Feedback): String {
        return feedback.error?.description ?: "Unknown"
    }
}
