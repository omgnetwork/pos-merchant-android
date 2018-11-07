package network.omisego.omgmerchant.pages.authorized.main

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 27/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import co.omisego.omisego.model.Token
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.whenever
import network.omisego.omgmerchant.NavBottomNavigationDirections
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.authorized.main.receive.ReceiveViewModel
import network.omisego.omgmerchant.pages.authorized.main.topup.TopupViewModel
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_TOPUP
import org.amshove.kluent.mock
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
    private val mainViewModel: MainViewModel by lazy { MainViewModel(mock(), mock()) }

    @Test
    fun `test createActionForScanPage from receive page should return action correctly`() {
        // Prepare
        val theCalculatorValueThatUserHas = "1000"
        val theSelectedToken = mock<Token>() // Whatever
        mainViewModel.currentCalculatorMode = CalculatorMode.RECEIVE

        // Mocking dependencies
        whenever(mockReceiveViewModel.liveCalculator).thenReturn(mock())
        whenever(mockReceiveViewModel.liveSelectedToken).thenReturn(mock())
        whenever(mockReceiveViewModel.liveCalculator.value).thenReturn(theCalculatorValueThatUserHas)
        whenever(mockReceiveViewModel.liveSelectedToken.value).thenReturn(theSelectedToken)

        // Verify
        val expectedAction = NavBottomNavigationDirections.ActionGlobalScanFragment(
            theSelectedToken
        )
            .setAmount(theCalculatorValueThatUserHas)
            .setTransactionType(SCAN_RECEIVE)

        mainViewModel.createActionForScanPage(mockReceiveViewModel, mockTopupViewModel) shouldEqual expectedAction
    }

    @Test
    fun `test createActionForScanPage from top-up page should return action correctly`() {
        // Prepare
        val theCalculatorValueThatUserHas = "1500"
        val theSelectedToken = mock<Token>() // Whatever
        mainViewModel.currentCalculatorMode = CalculatorMode.TOPUP

        // Mocking dependencies
        whenever(mockTopupViewModel.liveCalculator).thenReturn(mock())
        whenever(mockTopupViewModel.liveSelectedToken).thenReturn(mock())
        whenever(mockTopupViewModel.liveCalculator.value).thenReturn(theCalculatorValueThatUserHas)
        whenever(mockTopupViewModel.liveSelectedToken.value).thenReturn(theSelectedToken)

        // Verify
        val expectedAction = NavBottomNavigationDirections.ActionGlobalScanFragment(
            theSelectedToken
        )
            .setAmount(theCalculatorValueThatUserHas)
            .setTransactionType(SCAN_TOPUP)

        mainViewModel.createActionForScanPage(mockReceiveViewModel, mockTopupViewModel) shouldEqual expectedAction
    }

    @Test
    fun `test createActionForConfirmPage from receive page should return action correctly`() {
        // Prepare
        val theCalculatorValueThatUserHas = "1000"
        val theSelectedToken = mock<Token>() // Whatever
        mainViewModel.currentCalculatorMode = CalculatorMode.RECEIVE

        // Mocking dependencies
        whenever(mockReceiveViewModel.liveCalculator).thenReturn(mock())
        whenever(mockReceiveViewModel.liveSelectedToken).thenReturn(mock())
        whenever(mockReceiveViewModel.liveCalculator.value).thenReturn(theCalculatorValueThatUserHas)
        whenever(mockReceiveViewModel.liveSelectedToken.value).thenReturn(theSelectedToken)

        // Verify
        val expectedAction = NavBottomNavigationDirections.ActionGlobalConfirmFragment(
            theSelectedToken
        )
            .setAmount(theCalculatorValueThatUserHas)
            .setTransactionType(SCAN_RECEIVE)

        mainViewModel.createActionForConfirmPage(mockReceiveViewModel, mockTopupViewModel) shouldEqual expectedAction
    }

    @Test
    fun `test createActionForConfirmPage from top-up page should return action correctly`() {
        // Prepare
        val theCalculatorValueThatUserHas = "1500"
        val theSelectedToken = mock<Token>() // Whatever
        mainViewModel.currentCalculatorMode = CalculatorMode.TOPUP

        // Mocking dependencies
        whenever(mockTopupViewModel.liveCalculator).thenReturn(mock())
        whenever(mockTopupViewModel.liveSelectedToken).thenReturn(mock())
        whenever(mockTopupViewModel.liveCalculator.value).thenReturn(theCalculatorValueThatUserHas)
        whenever(mockTopupViewModel.liveSelectedToken.value).thenReturn(theSelectedToken)

        // Verify
        val expectedAction = NavBottomNavigationDirections.ActionGlobalConfirmFragment(
            theSelectedToken
        )
            .setAmount(theCalculatorValueThatUserHas)
            .setTransactionType(SCAN_TOPUP)

        mainViewModel.createActionForConfirmPage(mockReceiveViewModel, mockTopupViewModel) shouldEqual expectedAction
    }

    @Test
    fun `test createActionForFeedbackPage from main page should return action correctly`() {
        val mockFeedback = mock<Feedback>()

        val expectedAction = NavBottomNavigationDirections.ActionGlobalFeedbackFragment(mockFeedback)

        mainViewModel.createActionForFeedbackPage(mockFeedback) shouldEqual expectedAction
    }

    @Test
    fun `test Toolbar and BottomNavigationView should be gone on some specific destinations`() {
        // Prepare
        val anyController = mock<NavController>()
        val destSplash = mock<FragmentNavigator.Destination>().also { whenever(it.id).thenReturn(R.id.splashFragment) }
        val destSelectAccount = mock<FragmentNavigator.Destination>().also { whenever(it.id).thenReturn(R.id.selectAccountFragment) }
        val destScan = mock<FragmentNavigator.Destination>().also { whenever(it.id).thenReturn(R.id.scan) }
        val destConfirm = mock<FragmentNavigator.Destination>().also { whenever(it.id).thenReturn(R.id.confirmFragment) }
        val destFeedback = mock<FragmentNavigator.Destination>().also { whenever(it.id).thenReturn(R.id.feedbackFragment) }

        // Action and verify
        mainViewModel.fullScreenNavigatedListener.onNavigated(anyController, destSplash)
        mainViewModel.liveToolbarBottomNavVisibility.value shouldEqual View.GONE

        mainViewModel.fullScreenNavigatedListener.onNavigated(anyController, destSelectAccount)
        mainViewModel.liveToolbarBottomNavVisibility.value shouldEqual View.GONE

        mainViewModel.fullScreenNavigatedListener.onNavigated(anyController, destScan)
        mainViewModel.liveToolbarBottomNavVisibility.value shouldEqual View.GONE

        mainViewModel.fullScreenNavigatedListener.onNavigated(anyController, destConfirm)
        mainViewModel.liveToolbarBottomNavVisibility.value shouldEqual View.GONE

        mainViewModel.fullScreenNavigatedListener.onNavigated(anyController, destFeedback)
        mainViewModel.liveToolbarBottomNavVisibility.value shouldEqual View.GONE
    }

    @Test
    fun `test Toolbar and BottomNavigationView should be visible on some destinations`() {
        // Prepare
        val anyController = mock<NavController>()
        val destReceive = mock<FragmentNavigator.Destination>().also { whenever(it.id).thenReturn(R.id.receive) }
        val destTopup = mock<FragmentNavigator.Destination>().also { whenever(it.id).thenReturn(R.id.topup) }
        val destSetting = mock<FragmentNavigator.Destination>().also { whenever(it.id).thenReturn(R.id.more) }

        // Action and verify
        mainViewModel.fullScreenNavigatedListener.onNavigated(anyController, destReceive)
        mainViewModel.liveToolbarBottomNavVisibility.value shouldEqual View.VISIBLE

        mainViewModel.fullScreenNavigatedListener.onNavigated(anyController, destTopup)
        mainViewModel.liveToolbarBottomNavVisibility.value shouldEqual View.VISIBLE

        mainViewModel.fullScreenNavigatedListener.onNavigated(anyController, destSetting)
        mainViewModel.liveToolbarBottomNavVisibility.value shouldEqual View.VISIBLE
    }

    @Test
    fun `test next button should only be visible on receive and topup destination`() {
        // Prepare
        val anyController = mock<NavController>()
        val destReceive = mock<FragmentNavigator.Destination>().also { whenever(it.id).thenReturn(R.id.receive) }
        val destTopup = mock<FragmentNavigator.Destination>().also { whenever(it.id).thenReturn(R.id.topup) }
        val destScan = mock<FragmentNavigator.Destination>().also { whenever(it.id).thenReturn(R.id.scan) }
        val destSetting = mock<FragmentNavigator.Destination>().also { whenever(it.id).thenReturn(R.id.more) }

        // Action and verify
        mainViewModel.nextButtonNavigatedListener.onNavigated(anyController, destReceive)
        mainViewModel.liveShowNext.value shouldEqual true

        mainViewModel.nextButtonNavigatedListener.onNavigated(anyController, destTopup)
        mainViewModel.liveShowNext.value shouldEqual true

        mainViewModel.nextButtonNavigatedListener.onNavigated(anyController, destScan)
        mainViewModel.liveShowNext.value shouldEqual false

        mainViewModel.nextButtonNavigatedListener.onNavigated(anyController, destSetting)
        mainViewModel.liveShowNext.value shouldEqual false
    }

    @Test
    fun `test calculator mode should be set correctly depending on the destination is whether receive or topup`() {
        // Prepare
        val anyController = mock<NavController>()
        val destReceive = mock<FragmentNavigator.Destination>().also { whenever(it.id).thenReturn(R.id.receive) }
        val destTopup = mock<FragmentNavigator.Destination>().also { whenever(it.id).thenReturn(R.id.topup) }

        // Action and verify
        mainViewModel.calculatorModeNavigatedListener.onNavigated(anyController, destReceive)
        mainViewModel.currentCalculatorMode shouldEqual CalculatorMode.RECEIVE

        mainViewModel.calculatorModeNavigatedListener.onNavigated(anyController, destTopup)
        mainViewModel.currentCalculatorMode shouldEqual CalculatorMode.TOPUP
    }

    @Test
    fun `test the condition for showing select account destination should be correct`() {
        // Prepare
        val spiedMainViewModel = spy(mainViewModel)

        whenever(spiedMainViewModel.getAccount()).thenReturn(null)

        // Action and verify
        spiedMainViewModel.meetDestination() shouldEqual MainViewModel.DestinationCondition.DEST_SELECT_ACCOUNT
    }

    @Test
    fun `test the condition for showing splash destination should be correct`() {
        // Prepare
        val spiedMainViewModel = spy(mainViewModel)

        whenever(spiedMainViewModel.getAccount()).thenReturn(mock())
        spiedMainViewModel.showSplash = true

        // Action and verify
        spiedMainViewModel.meetDestination() shouldEqual MainViewModel.DestinationCondition.DEST_SPLASH
    }

    @Test
    fun `test the condition for showing main destination should be correct`() {
        // Prepare
        val spiedMainViewModel = spy(mainViewModel)

        whenever(spiedMainViewModel.getAccount()).thenReturn(mock())
        spiedMainViewModel.showSplash = false

        // Action and verify
        spiedMainViewModel.meetDestination() shouldEqual MainViewModel.DestinationCondition.DEST_MAIN
    }

    @Test
    fun `test display depending on destination condition`() {
        // Prepare
        val spiedMainViewModel = spy(mainViewModel)

        // Action and verify

        /* Select account */
        doReturn(MainViewModel.DestinationCondition.DEST_SELECT_ACCOUNT).`when`(spiedMainViewModel).meetDestination()
        spiedMainViewModel.displayOtherDestinationByCondition()
        spiedMainViewModel.liveDestinationId.value?.peekContent() shouldEqual R.id.action_global_selectAccountFragment

        /* Splash */
        whenever(spiedMainViewModel.meetDestination()).thenReturn(MainViewModel.DestinationCondition.DEST_SPLASH)
        spiedMainViewModel.displayOtherDestinationByCondition()
        spiedMainViewModel.liveDestinationId.value?.peekContent() shouldEqual R.id.action_global_splashFragment
    }

    /*  ------------------ Move to another test file ------------------- */

    // Should be moved to receive or topup tests
//    @Test
//    fun `next button should be disabled when the calculator value is 'zero|special_char' in top-up page`() {
//        val liveCalculator = LiveCalculator("0")
//        val topupViewModel = TopupViewModel()
//
//        mainViewModel.handleEnableNextButtonByPager(liveCalculator, CalculatorMode.TOPUP)
//
//        mainViewModel.liveEnableNext.value shouldBe false
//    }
//
//    @Test
//    fun `next button should be enabled when the calculator value is non-zero in top-up page`() {
//        val liveCalculator = LiveCalculator("1234")
//
////        mainViewModel.handleEnableNextButtonByPager(liveCalculator, PAGE_TOPUP)
//
//        mainViewModel.liveEnableNext.value shouldBe true
//    }

//    @Test
//    fun `next button should be disabled when the calculator value is zero in receive page`() {
//        val liveCalculator = LiveCalculator("0")
//
////        mainViewModel.handleEnableNextButtonByPager(liveCalculator, PAGE_RECEIVE)
//
//        mainViewModel.liveEnableNext.value shouldBe false
//    }
//
//    @Test
//    fun `next button should be disabled when the calculator value is containing operator in receive page`() {
//        val liveCalculator = LiveCalculator("1234+4567")
//
////        mainViewModel.handleEnableNextButtonByPager(liveCalculator, PAGE_RECEIVE)
//        mainViewModel.liveEnableNext.value shouldBe false
//
//        liveCalculator.value = "-1234"
////        mainViewModel.handleEnableNextButtonByPager(liveCalculator, PAGE_RECEIVE)
//        mainViewModel.liveEnableNext.value shouldBe false
//    }
//
//    @Test
//    fun `next button should be enabled when the calculator value is not containing operator and non-zero in receive page`() {
//        val liveCalculator = LiveCalculator("1234")
//
////        mainViewModel.handleEnableNextButtonByPager(liveCalculator, PAGE_RECEIVE)
//        mainViewModel.liveEnableNext.value shouldBe true
//    }
}
