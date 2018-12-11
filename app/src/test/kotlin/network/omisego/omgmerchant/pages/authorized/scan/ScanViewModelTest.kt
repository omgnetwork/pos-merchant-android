package network.omisego.omgmerchant.pages.authorized.scan

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 2/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import co.omisego.omisego.extension.bd
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.helper.HelperFormatter
import network.omisego.omgmerchant.model.AmountFormat
import network.omisego.omgmerchant.pages.authorized.scan.handler.AbstractScanHandler
import network.omisego.omgmerchant.pages.authorized.scan.handler.HandlerGetTransactionRequest
import network.omisego.omgmerchant.pages.authorized.scan.handler.HandlerGetUserWallet
import network.omisego.omgmerchant.repository.RemoteRepository
import org.amshove.kluent.any
import org.amshove.kluent.mock
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqualTo
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23])
class ScanViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    val mockAmountFormatter: HelperFormatter = mock()
    val mockRepository: RemoteRepository = mock()
    private val viewModel: ScanViewModel by lazy { ScanViewModel(RuntimeEnvironment.application, mockAmountFormatter, mockRepository) }
    private val mockArgs: ScanFragmentArgs by lazy { mock<ScanFragmentArgs>() }

    @Test
    fun `test amountText should call amount formatter`() {
        viewModel.args = mockArgs
        whenever(mockArgs.amount).thenReturn("100")
        whenever(mockArgs.token).thenReturn(mock())
        whenever(mockArgs.token.subunitToUnit).thenReturn(1.bd)
        whenever(mockArgs.token.symbol).thenReturn("OMG")
        whenever(mockAmountFormatter.formatDisplayAmount(any(), any())).thenReturn("100 OMG")

        viewModel.amountText shouldEqualTo "100 OMG"

        verify(mockAmountFormatter, times(1)).formatDisplayAmount(
            AmountFormat.Unit(mockArgs.amount.toBigDecimal(), mockArgs.token.subunitToUnit), mockArgs.token
        )
    }

    @Test
    fun `test title should be displayed correctly`() {
        viewModel.args = mockArgs

        whenever(mockArgs.transactionType).thenReturn(SCAN_RECEIVE)
        viewModel.title shouldEqualTo RuntimeEnvironment.application.getString(R.string.scan_title_payment)

        whenever(mockArgs.transactionType).thenReturn(SCAN_TOPUP)
        viewModel.title shouldEqualTo RuntimeEnvironment.application.getString(R.string.scan_title_topup)
    }

    @Test
    fun `test decode the qr code payload should call retrieveUserInformation`() {
        val spiedScanViewModel = spy(viewModel)
        val handler = mock<AbstractScanHandler>()
        val payload = "something"

        doReturn(handler).`when`(spiedScanViewModel).findScanPayloadHandler(payload)

        spiedScanViewModel.onDecoded(payload)

        verify(handler).retrieve(payload)
    }

    @Test
    fun `test find payload handler should be returned an appropriate handler`() {
        val payloadTransactionRequest = "txr_1234abcd"
        val payloadAddress = "aabbccdd"

        viewModel.args = mock()
        viewModel.liveDirection = mock()

        viewModel.findScanPayloadHandler(payloadTransactionRequest) shouldBeInstanceOf HandlerGetTransactionRequest::class
        viewModel.findScanPayloadHandler(payloadAddress) shouldBeInstanceOf HandlerGetUserWallet::class
    }
}
