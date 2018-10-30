package network.omisego.omgmerchant.pages.authorized.main

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 27/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import co.omisego.omisego.model.Token
import com.nhaarman.mockito_kotlin.whenever
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.model.LiveCalculator
import network.omisego.omgmerchant.pages.authorized.main.receive.ReceiveViewModel
import network.omisego.omgmerchant.pages.authorized.main.topup.TopupViewModel
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_TOPUP
import org.amshove.kluent.mock
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MainViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val mockReceiveViewModel: ReceiveViewModel by lazy { mock<ReceiveViewModel>() }
    private val mockTopupViewModel: TopupViewModel by lazy { mock<TopupViewModel>() }
    private val liveToken: MutableLiveData<Token> = MutableLiveData()
    private val mainViewModel: MainViewModel by lazy { MainViewModel(mock(), mock(), mock()) }

    @Test
    fun `createActionForScanPage from receive page should return action correctly`() {
        mainViewModel.livePage.value = PAGE_RECEIVE

        val mockTokenReceive: Token = mock()

        whenever(mockReceiveViewModel.liveCalculator).thenReturn(mock())
        whenever(mockReceiveViewModel.liveCalculator.value).thenReturn("1000")
        whenever(mockReceiveViewModel.liveToken).thenReturn(liveToken)
        liveToken.value = mockTokenReceive

        val expectedAction = MainFragmentDirections.ActionMainToScan(
            mockTokenReceive
        )
            .setAmount("1000")
            .setTransactionType(SCAN_RECEIVE)

        mainViewModel.createActionForScanPage(mockReceiveViewModel, mockTopupViewModel) shouldEqual expectedAction
    }

    @Test
    fun `createActionForScanPage from top-up page should return action correctly`() {
        mainViewModel.livePage.value = PAGE_TOPUP

        val mockTokenTopup: Token = mock()

        whenever(mockTopupViewModel.liveCalculator).thenReturn(mock())
        whenever(mockTopupViewModel.liveCalculator.value).thenReturn("1500")
        whenever(mockTopupViewModel.liveToken).thenReturn(mock())
        whenever(mockTopupViewModel.liveToken.value).thenReturn(mockTokenTopup)

        val expectedAction = MainFragmentDirections.ActionMainToScan(
            mockTokenTopup
        )
            .setAmount("1500")
            .setTransactionType(SCAN_TOPUP)

        mainViewModel.createActionForScanPage(mockReceiveViewModel, mockTopupViewModel) shouldEqual expectedAction
    }

    @Test
    fun `createActionForFeedbackPage from main page should return action correctly`() {
        val mockFeedback = mock<Feedback>()

        whenever(mainViewModel.mainRepository.getFeedback()).thenReturn(mockFeedback)

        val expectedAction = MainFragmentDirections.ActionMainToFeedback(mockFeedback)

        mainViewModel.createActionForFeedbackPage() shouldEqual expectedAction
    }

    @Test
    fun `next button should be disabled when the calculator value is zero in top-up page`() {
        val liveCalculator = LiveCalculator("0")

        mainViewModel.handleEnableNextButtonByPager(liveCalculator, PAGE_TOPUP)

        mainViewModel.liveEnableNext.value shouldBe false
    }

    @Test
    fun `next button should be enabled when the calculator value is non-zero in top-up page`() {
        val liveCalculator = LiveCalculator("1234")

        mainViewModel.handleEnableNextButtonByPager(liveCalculator, PAGE_TOPUP)

        mainViewModel.liveEnableNext.value shouldBe true
    }

    @Test
    fun `next button should be disabled when the calculator value is zero in receive page`() {
        val liveCalculator = LiveCalculator("0")

        mainViewModel.handleEnableNextButtonByPager(liveCalculator, PAGE_RECEIVE)

        mainViewModel.liveEnableNext.value shouldBe false
    }

    @Test
    fun `next button should be disabled when the calculator value is containing operator in receive page`() {
        val liveCalculator = LiveCalculator("1234+4567")

        mainViewModel.handleEnableNextButtonByPager(liveCalculator, PAGE_RECEIVE)
        mainViewModel.liveEnableNext.value shouldBe false

        liveCalculator.value = "-1234"
        mainViewModel.handleEnableNextButtonByPager(liveCalculator, PAGE_RECEIVE)
        mainViewModel.liveEnableNext.value shouldBe false
    }

    @Test
    fun `next button should be enabled when the calculator value is not containing operator and non-zero in receive page`(){
        val liveCalculator = LiveCalculator("1234")

        mainViewModel.handleEnableNextButtonByPager(liveCalculator, PAGE_RECEIVE)
        mainViewModel.liveEnableNext.value shouldBe true
    }
}
