package network.omisego.omgmerchant.pages.authorized.confirm.handler

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.extension.bd
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.Transaction
import co.omisego.omisego.model.params.admin.TransactionCreateParams
import co.omisego.omisego.utils.GsonProvider
import com.google.gson.reflect.TypeToken
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.timeout
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import network.omisego.omgmerchant.helper.ResourceFile
import network.omisego.omgmerchant.helper.mockEnqueueWithHttpCode
import network.omisego.omgmerchant.network.ClientProvider
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer
import org.amshove.kluent.mock
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
class HandlerCreateTransactionTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var mockUrl: HttpUrl
    private lateinit var mockWebServer: MockWebServer
    private val gson by lazy { GsonProvider.create() }
    private val mockLocalRepository: LocalRepository = mock()
    private val fileTransaction: File by ResourceFile("transaction.json")
    private val fileTransactionError: File by ResourceFile("transaction_error.json")
    private val handlerCreateTransaction: HandlerCreateTransaction by lazy {
        HandlerCreateTransaction(mockLocalRepository, RemoteRepository()).apply {
            args = mock()
            liveDirection = MutableLiveData()
        }
    }

    @Before
    fun setup() {
        initMockWebServer()
        ClientProvider.client = ClientProvider.create(mockUrl, Executor { it.run() })
    }

    @Test
    fun `test handle payload should call transfer and handle a successful result correctly`() {
        fileTransaction.mockEnqueueWithHttpCode(mockWebServer)

        val spiedHandler = spy(handlerCreateTransaction)
        val mockTransactionCreateParams = mock<TransactionCreateParams>()

        doReturn(mockTransactionCreateParams).`when`(spiedHandler).createTransactionCreateParams("payload")
        whenever(handlerCreateTransaction.args.transactionType).thenReturn(SCAN_RECEIVE)

        spiedHandler.onHandlePayload("payload")

        val typeTransactionToken = object : TypeToken<OMGResponse<Transaction>>() {}.type
        val responseTransaction = gson.fromJson<OMGResponse<Transaction>>(fileTransaction.readText(), typeTransactionToken)
        verify(spiedHandler, timeout(3000).times(1)).handleSucceedToHandlePayload(responseTransaction.data)
    }

    @Test
    fun `test handle payload should call transfer and handle a failed result correctly`() {
        fileTransactionError.mockEnqueueWithHttpCode(mockWebServer)

        val spiedHandler = spy(handlerCreateTransaction)
        val mockTransactionCreateParams = mock<TransactionCreateParams>()

        doReturn(mockTransactionCreateParams).`when`(spiedHandler).createTransactionCreateParams("payload")
        whenever(handlerCreateTransaction.args.transactionType).thenReturn(SCAN_RECEIVE)
        whenever(handlerCreateTransaction.args.token).thenReturn(mock())
        whenever(handlerCreateTransaction.args.token.id).thenReturn("OMG")
        whenever(handlerCreateTransaction.args.token.subunitToUnit).thenReturn(100.bd)
        whenever(handlerCreateTransaction.args.amount).thenReturn("100")

        spiedHandler.onHandlePayload("payload")

        val typeTransactionToken = object : TypeToken<OMGResponse<APIError>>() {}.type
        val responseTransaction = gson.fromJson<OMGResponse<APIError>>(fileTransactionError.readText(), typeTransactionToken)
        verify(spiedHandler, timeout(3000).times(1)).handleFailToHandlePayload(responseTransaction.data)
    }

    private fun initMockWebServer() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        mockUrl = mockWebServer.url("/api/admin/")
    }
}
