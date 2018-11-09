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
import network.omisego.omgmerchant.model.AmountFormat
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

    fun transformIconText(context: Context, feedback: Feedback): String {
        return when (feedback.success) {
            true -> context.getString(R.string.feedback_icon_success)
            false -> context.getString(R.string.feedback_icon_failed)
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
        val subunit = AmountFormat.Subunit(feedback.source.amount, feedback.source.token.subunitToUnit)
        return String.format(
            "%s %s",
            subunit.toUnit().display(),
            feedback.source.token.symbol
        )
    }

    fun transformUserId(context: Context, feedback: Feedback): String {
        return context.getString(R.string.feedback_customer_id, feedback.source.userId
            ?: context.getString(R.string.feedback_error_code_unknown))
    }

    fun transformUserName(context: Context, feedback: Feedback): String {
        return context.getString(
            R.string.feedback_customer_name,
            (feedback.source.user?.email ?: feedback.source.user?.username
            ?: context.getString(R.string.feedback_error_code_unknown))
        )
    }

    fun transformDate(context: Context, feedback: Feedback): String {
        return context.getString(R.string.feedback_date_time, feedback.createdAt)
    }

    fun transformErrorCode(context: Context, feedback: Feedback): String {
        return context.getString(R.string.feedback_error_code, feedback.error?.code?.name?.toUpperCase()
            ?: context.getString(R.string.feedback_error_code_unknown))
    }

    fun transformErrorDescription(context: Context, feedback: Feedback): String {
        return feedback.error?.description ?: context.getString(R.string.feedback_error_code_unknown)
    }
}
