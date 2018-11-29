package network.omisego.omgmerchant.pages.authorized.confirm.handler

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.constant.enums.ErrorCode
import co.omisego.omisego.extension.bd
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.TransactionConsumption
import co.omisego.omisego.model.params.admin.TransactionConsumptionParams
import co.omisego.omisego.utils.GsonProvider
import com.google.gson.reflect.TypeToken
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.timeout
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import network.omisego.omgmerchant.NavBottomNavigationDirections
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.helper.ResourceFile
import network.omisego.omgmerchant.helper.mockEnqueueWithHttpCode
import network.omisego.omgmerchant.helper.stringRes
import network.omisego.omgmerchant.model.AmountFormat
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.network.ClientProvider
import network.omisego.omgmerchant.network.ParamsCreator
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer
import org.amshove.kluent.mock
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.util.concurrent.Executor

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23])
class HandlerConsumeTransactionRequestTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var mockUrl: HttpUrl
    private lateinit var mockWebServer: MockWebServer
    private val fileTransactionConsumption: File by ResourceFile("transaction_consumption.json")
    private val fileRejectedTransactionConsumption: File by ResourceFile("rejected_transaction_consumption.json")
    private val fileAPIError: File by ResourceFile("transaction_error.json")
    private val gson by lazy { GsonProvider.create() }
    private val mockLocalRepository: LocalRepository = mock()

    private val responseTransactionConsumption by lazy {
        val txConsumptionToken = object : TypeToken<OMGResponse<TransactionConsumption>>() {}.type
        gson.fromJson<OMGResponse<TransactionConsumption>>(fileTransactionConsumption.readText(), txConsumptionToken)
    }

    private val responseRejectedTransactionConsumption by lazy {
        val txConsumptionToken = object : TypeToken<OMGResponse<TransactionConsumption>>() {}.type
        gson.fromJson<OMGResponse<TransactionConsumption>>(fileRejectedTransactionConsumption.readText(), txConsumptionToken)
    }

    private val responseAPIError by lazy {
        val error = object : TypeToken<OMGResponse<APIError>>() {}.type
        gson.fromJson<OMGResponse<APIError>>(fileAPIError.readText(), error)
    }

    private val handler: HandlerConsumeTransactionRequest by lazy {
        HandlerConsumeTransactionRequest(mockLocalRepository, RemoteRepository())
    }

    private val mockParams = mock<TransactionConsumptionParams>().apply {
        whenever(amount).thenReturn(100000.bd)
        whenever(tokenId).thenReturn("omg_id")
    }

    private val mockParamsCreator = mock<ParamsCreator>().apply {
        whenever(createTransactionConsumptionParams(
            "payload",
            AmountFormat.Unit(1000.bd, 100.bd),
            "omg_id",
            null,
            null,
            null
        )).thenReturn(mockParams)
    }

    private val spiedHandler = spy(handler).apply {
        this.args = mock()
        this.liveDirection = MutableLiveData()
    }

    @Before
    fun setup() {
        initMockWebServer()
        ClientProvider.client = ClientProvider.create(mockUrl, Executor { it.run() })
    }

    @Test
    fun `test handle payload should manage transaction consumption with non-required confirmation correctly`() {
        fileTransactionConsumption.mockEnqueueWithHttpCode(mockWebServer)

        whenever(spiedHandler.paramsCreator).thenReturn(mockParamsCreator)
        mockLocalRepository()
        mockHandlerArg(spiedHandler)

        spiedHandler.onHandlePayload("payload")

        verify(mockParamsCreator).createTransactionConsumptionParams(
            "payload",
            AmountFormat.Unit(1000.bd, 100.bd),
            "omg_id",
            null,
            null,
            null
        )

        verify(spiedHandler, timeout(3000).times(1)).handleSucceedToHandlePayload(
            responseTransactionConsumption.data
        )
    }

    @Test
    fun `test handle payload should manage failed transaction consumption correctly`() {
        fileAPIError.mockEnqueueWithHttpCode(mockWebServer)

        whenever(spiedHandler.paramsCreator).thenReturn(mockParamsCreator)
        mockLocalRepository()
        mockHandlerArg(spiedHandler)

        spiedHandler.onHandlePayload("payload")

        verify(spiedHandler, timeout(3000).times(1)).handleFailToHandlePayload(responseAPIError.data)
    }

    @Test
    fun `test handle confirmed transaction consumption correctly`() {
        mockHandlerArg(spiedHandler)

        spiedHandler.handleSucceedToHandlePayload(responseTransactionConsumption.data)

        spiedHandler.liveDirection.value?.peekContent() shouldEqual NavBottomNavigationDirections.ActionGlobalFeedbackFragment(
            Feedback.success(SCAN_RECEIVE, responseTransactionConsumption.data)
        )
    }

    @Test
    fun `test handle rejected transaction consumption correctly`() {
        mockHandlerArg(spiedHandler)

        val data = responseRejectedTransactionConsumption.data
        spiedHandler.handleSucceedToHandlePayload(data)

         spiedHandler.liveDirection.value?.peekContent().toString().trim() shouldEqual NavBottomNavigationDirections.ActionGlobalFeedbackFragment(
            Feedback.error(
                spiedHandler.args,
                data.transactionRequest.address,
                data.transactionRequest.user,
                APIError(ErrorCode.SDK_UNEXPECTED_ERROR, stringRes(R.string.feedback_user_reject))
            )
        ).toString().trim()
    }

    @Test
    fun `test handle payload when receive APIError`() {
        mockHandlerArg(spiedHandler)
        spiedHandler.handleFailToHandlePayload(responseAPIError.data)
        spiedHandler.liveDirection.value?.peekContent().toString().trim() shouldEqual NavBottomNavigationDirections.ActionGlobalFeedbackFragment(
            Feedback.error(spiedHandler.args, null, spiedHandler.args.user, responseAPIError.data)
        ).toString().trim()
    }

    private fun mockLocalRepository() {
        whenever(mockLocalRepository.loadAccount()).thenReturn(null)
        whenever(mockLocalRepository.loadWallet()).thenReturn(null)
        whenever(mockLocalRepository.loadAccount()).thenReturn(null)
    }

    private fun mockHandlerArg(mockHandler: HandlerConsumeTransactionRequest) {
        whenever(mockHandler.args.amount).thenReturn("1000")
        whenever(mockHandler.args.token).thenReturn(mock())
        whenever(mockHandler.args.user).thenReturn(mock())
        whenever(mockHandler.args.user.id).thenReturn("user_id")
        whenever(mockHandler.args.transactionType).thenReturn(SCAN_RECEIVE)
        whenever(mockHandler.args.token.subunitToUnit).thenReturn(100.bd)
        whenever(mockHandler.args.token.id).thenReturn("omg_id")
    }

    private fun initMockWebServer() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        mockUrl = mockWebServer.url("/api/admin/")
    }
}
