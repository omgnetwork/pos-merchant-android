package network.omisego.omgmerchant.pages.authorized.feedback

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Context
import com.nhaarman.mockito_kotlin.whenever
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_TOPUP
import network.omisego.omgmerchant.helper.drawableRes
import network.omisego.omgmerchant.helper.stringRes
import org.amshove.kluent.mock
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.Date

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23])
class FeedbackTransformerTest {

    private val mockFeedback: Feedback by lazy { mock<Feedback>() }
    private val transformer: FeedbackTransformer by lazy { FeedbackTransformer() }
    private val context: Context by lazy { RuntimeEnvironment.application }

    @Test
    fun `test icon should be returned correctly`() {
        whenever(mockFeedback.success).thenReturn(true)
        transformer.transformIcon(context, mockFeedback) shouldEqual drawableRes(R.drawable.shape_circle_solid_blue)
        whenever(mockFeedback.success).thenReturn(false)
        transformer.transformIcon(context, mockFeedback) shouldEqual drawableRes(R.drawable.shape_circle_solid_red)
    }

    @Test
    fun `test icon text should be returned correctly`() {
        whenever(mockFeedback.success).thenReturn(true)
        transformer.transformIconText(context, mockFeedback) shouldEqualTo stringRes(R.string.feedback_icon_success)
        whenever(mockFeedback.success).thenReturn(false)
        transformer.transformIconText(context, mockFeedback) shouldEqualTo stringRes(R.string.feedback_icon_failed)
    }

    @Test
    fun `test title should be depend on transaction type and status correctly`() {
        // payment and success
        whenever(mockFeedback.success).thenReturn(true)
        whenever(mockFeedback.transactionType).thenReturn(SCAN_RECEIVE)
        transformer.transformTitle(context, mockFeedback) shouldEqualTo stringRes(R.string.feedback_success_payment_title)

        // payment and fail
        whenever(mockFeedback.success).thenReturn(false)
        whenever(mockFeedback.transactionType).thenReturn(SCAN_RECEIVE)
        transformer.transformTitle(context, mockFeedback) shouldEqualTo stringRes(R.string.feedback_fail_payment_title)

        // topup and success
        whenever(mockFeedback.success).thenReturn(true)
        whenever(mockFeedback.transactionType).thenReturn(SCAN_TOPUP)
        transformer.transformTitle(context, mockFeedback) shouldEqualTo stringRes(R.string.feedback_success_topup_title)

        // topup and fail
        whenever(mockFeedback.success).thenReturn(false)
        whenever(mockFeedback.transactionType).thenReturn(SCAN_TOPUP)
        transformer.transformTitle(context, mockFeedback) shouldEqualTo stringRes(R.string.feedback_fail_topup_title)
    }

    @Test
    fun `test user id should be returned correctly`() {
        whenever(mockFeedback.source).thenReturn(mock())
        whenever(mockFeedback.source.userId).thenReturn("user_01")
        transformer.transformUserId(context, mockFeedback) shouldEqualTo context.getString(R.string.feedback_customer_id, "user_01")
    }

    @Test
    fun `test username should be returned correctly`() {
        whenever(mockFeedback.source).thenReturn(mock())
        whenever(mockFeedback.source.user).thenReturn(mock())
        whenever(mockFeedback.source.user?.username).thenReturn("username")

        whenever(mockFeedback.source.user?.email).thenReturn("user@email.com")
        transformer.transformUserName(context, mockFeedback) shouldEqualTo context.getString(R.string.feedback_customer_name, "user@email.com")

        whenever(mockFeedback.source.user?.email).thenReturn(null)
        transformer.transformUserName(context, mockFeedback) shouldEqualTo context.getString(R.string.feedback_customer_name, "username")
    }

    @Test
    fun `test date should be returned correctly`() {
        val date = Date()
        whenever(mockFeedback.createdAt).thenReturn(date)

        transformer.transformDate(context, mockFeedback) shouldEqualTo context.getString(R.string.feedback_date_time, date)
    }

    @Test
    fun `test error code should be returned correctly`() {
        whenever(mockFeedback.error).thenReturn(mock())
        whenever(mockFeedback.error?.code).thenReturn(mock())

        whenever(mockFeedback.error?.code?.name).thenReturn("MY_ERROR")
        transformer.transformErrorCode(context, mockFeedback) shouldEqualTo context.getString(R.string.feedback_error_code, "MY_ERROR")

        whenever(mockFeedback.error?.code?.name).thenReturn(null)
        transformer.transformErrorCode(context, mockFeedback) shouldEqualTo context.getString(R.string.feedback_error_code, stringRes(R.string.feedback_error_code_unknown))
    }

    @Test
    fun `test error description should be returned correctly`() {
        whenever(mockFeedback.error).thenReturn(mock())

        whenever(mockFeedback.error?.description).thenReturn("my error description")
        transformer.transformErrorDescription(context, mockFeedback) shouldEqualTo "my error description"

        whenever(mockFeedback.error?.description).thenReturn(null)
        transformer.transformErrorDescription(context, mockFeedback) shouldEqualTo stringRes(R.string.feedback_error_code_unknown)
    }
}
