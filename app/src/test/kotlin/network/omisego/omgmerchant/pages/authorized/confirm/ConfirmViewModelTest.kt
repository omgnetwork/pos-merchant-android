package network.omisego.omgmerchant.pages.authorized.confirm

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 2/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.view.View
import co.omisego.omisego.extension.bd
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.helper.stringRes
import network.omisego.omgmerchant.pages.authorized.confirm.handler.AbstractConfirmHandler
import network.omisego.omgmerchant.pages.authorized.confirm.handler.HandlerConsumeTransactionRequest
import network.omisego.omgmerchant.pages.authorized.confirm.handler.HandlerCreateTransaction
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_TOPUP
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository
import org.amshove.kluent.mock
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23])
class ConfirmViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    val mockRemoteRepository: RemoteRepository = mock()
    val mockLocalRepository: LocalRepository = mock()
    private val viewModel: ConfirmViewModel by lazy { ConfirmViewModel(RuntimeEnvironment.application, mockLocalRepository, mockRemoteRepository) }
    private val mockArgs: ConfirmFragmentArgs by lazy { mock<ConfirmFragmentArgs>() }

    @Before
    fun setup() {
        viewModel.args = mockArgs
    }

    @Test
    fun `test amount text is displayed correctly`() {
        whenever(mockArgs.amount).thenReturn("100")
        whenever(mockArgs.token).thenReturn(mock())
        whenever(mockArgs.token.subunitToUnit).thenReturn(1.bd)
        whenever(mockArgs.token.symbol).thenReturn("OMG")

        viewModel.amountText shouldEqualTo "100 OMG"
    }

    @Test
    fun `test transaction direction text is displayed correctly`() {
        whenever(mockArgs.transactionType).thenReturn(SCAN_RECEIVE)
        viewModel.transactionDirectionText shouldEqualTo stringRes(R.string.confirm_transaction_direction_from)

        whenever(mockArgs.transactionType).thenReturn(SCAN_TOPUP)
        viewModel.transactionDirectionText shouldEqualTo stringRes(R.string.confirm_transaction_direction_to)
    }

    @Test
    fun `test transaction type text is displayed correctly`() {
        whenever(mockArgs.transactionType).thenReturn(SCAN_RECEIVE)
        viewModel.transactionTypeText shouldEqualTo stringRes(R.string.confirm_transaction_type_receive)

        whenever(mockArgs.transactionType).thenReturn(SCAN_TOPUP)
        viewModel.transactionTypeText shouldEqualTo stringRes(R.string.confirm_transaction_type_top_up)
    }

    @Test
    fun `test when yes button is clicked then the LiveData value should be changed`() {
        val mockView = mock<View>()
        viewModel.handleYesClick(mockView)
        viewModel.liveYesClick.value?.peekContent() shouldEqual mockView
    }

    @Test
    fun `test when no button is clicked then the LiveData value should be changed`() {
        val mockView = mock<View>()
        viewModel.handleNoClick(mockView)
        viewModel.liveNoClick.value?.peekContent() shouldEqual mockView
    }

    @Test
    fun `test handle payload`() {
        val spiedConfirmViewModel = spy(viewModel)
        val mockHandler: AbstractConfirmHandler = mock()
        spiedConfirmViewModel.args = mock()
        spiedConfirmViewModel.liveDirection = MutableLiveData()

        whenever(spiedConfirmViewModel.args.address).thenReturn("address")
        doReturn(mockHandler).`when`(spiedConfirmViewModel).findConfirmHandler()

        spiedConfirmViewModel.handleQRPayload()

        verify(mockHandler).createDestinationLoading()
        verify(mockHandler).args = spiedConfirmViewModel.args
        verify(mockHandler).onHandlePayload("address")
    }

    @Test
    fun `test find a confirm handler correctly`() {
        viewModel.args = mock()
        viewModel.liveDirection = MutableLiveData()
        viewModel.liveCancelTransactionConsumptionId = MutableLiveData()

        whenever(viewModel.args.address).thenReturn("txr_transaction_request_id")
        viewModel.findConfirmHandler() shouldBeInstanceOf HandlerConsumeTransactionRequest::class

        whenever(viewModel.args.address).thenReturn("addr1234")
        viewModel.findConfirmHandler() shouldBeInstanceOf HandlerCreateTransaction::class
    }
}
