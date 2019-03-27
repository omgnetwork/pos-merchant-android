package network.omisego.omgmerchant.pages.authorized.feedback

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.TransactionConsumption
import co.omisego.omisego.utils.GsonProvider
import com.google.gson.reflect.TypeToken
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import network.omisego.omgmerchant.helper.ResourceFile
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23])
class FeedbackViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val app = RuntimeEnvironment.application
    private val gson by lazy { GsonProvider.create() }
    private val mockTransformer: FeedbackTransformer by lazy { mock<FeedbackTransformer>() }
    private val fileTransactionConsumption: File by ResourceFile("transaction_consumption.json")

    private val responseTransactionConsumption by lazy {
        val txConsumptionToken = object : TypeToken<OMGResponse<TransactionConsumption>>() {}.type
        gson.fromJson<OMGResponse<TransactionConsumption>>(fileTransactionConsumption.readText(), txConsumptionToken)
    }

    private val viewModel: FeedbackViewModel by lazy {
        FeedbackViewModel(
            app,
            mockTransformer
        )
    }

    private val mockFeedback: Feedback by lazy {
        Feedback.success(
            SCAN_RECEIVE,
            responseTransactionConsumption.data
        )
    }

    @Before
    fun setup() {
        viewModel.liveFeedback.value = mockFeedback
    }

    @Test
    fun `test liveFeedback should be transformed to icon correctly`() {
        // The transformation is working lazily when observing is called
        viewModel.icon.observeForever { }
        verify(mockTransformer).transformIcon(app, mockFeedback)
    }

    @Test
    fun `test liveFeedback should be transformed to iconText correctly`() {
        viewModel.iconText.observeForever { }
        verify(mockTransformer).transformIconText(app, mockFeedback)
    }

    @Test
    fun `test liveFeedback should be transformed to title correctly`() {
        viewModel.title.observeForever { }
        verify(mockTransformer).transformTitle(app, mockFeedback)
    }

    @Test
    fun `test liveFeedback should be transformed to amount correctly`() {
        viewModel.amount.observeForever { }
        verify(mockTransformer).transformAmount(app, mockFeedback)
    }

    @Test
    fun `test liveFeedback should be transformed to userId correctly`() {
        viewModel.id.observeForever { }
        verify(mockTransformer).transformId(app, mockFeedback)
    }

    @Test
    fun `test liveFeedback should be transformed to username correctly`() {
        viewModel.name.observeForever { }
        verify(mockTransformer).transformName(app, mockFeedback)
    }

    @Test
    fun `test liveFeedback should be transformed to date correctly`() {
        viewModel.date.observeForever { }
        verify(mockTransformer).transformDate(app, mockFeedback)
    }

    @Test
    fun `test liveFeedback should be transformed to errorCode correctly`() {
        viewModel.errorCode.observeForever { }
        verify(mockTransformer).transformErrorCode(app, mockFeedback)
    }

    @Test
    fun `test liveFeedback should be transformed to errorDescription correctly`() {
        viewModel.errorDescription.observeForever { }
        verify(mockTransformer).transformErrorDescription(app, mockFeedback)
    }

    @Test
    fun `test liveFeedback should be transformed to liveShowError correctly`() {
        viewModel.liveShowError.observeForever { }
        viewModel.liveShowError.value shouldEqual false
    }
}
