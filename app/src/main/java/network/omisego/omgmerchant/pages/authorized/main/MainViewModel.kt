package network.omisego.omgmerchant.pages.authorized.main

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 15/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import androidx.navigation.NavController
import co.omisego.omisego.model.params.TokenListParams
import network.omisego.omgmerchant.NavBottomNavigationDirections
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository
import network.omisego.omgmerchant.extensions.fetchedThenCache
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.authorized.main.receive.ReceiveViewModel
import network.omisego.omgmerchant.pages.authorized.main.topup.TopupViewModel
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_TOPUP

class MainViewModel(
    internal val localRepository: LocalRepository,
    val remoteRepository: RemoteRepository
) : ViewModel() {
    internal var showSplash = true
    internal var currentCalculatorMode: CalculatorMode = CalculatorMode.RECEIVE

    /* LiveData */
    val liveTokenAPIResult: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }

    /* Control next button ui */
    val liveEnableNext: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val liveShowNext: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    /* Control navigation to conditional destination e.g. the user hasn't load an account yet, should go to select account page. */
    val liveDestinationId: MutableLiveData<Event<Int>> by lazy { MutableLiveData<Event<Int>>() }

    /* Control whether should hide toolbar or bottom navigation e.g. the select account page has its own toolbar, so we don't need to show it. */
    val liveToolbarBottomNavVisibility: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    /* Control feedback object which is set from the confirmation page */
    val liveFeedback: MutableLiveData<Feedback> by lazy { MutableLiveData<Feedback>() }

    /* Control transaction status */
    val liveLoading: MutableLiveData<Event<Boolean>> by lazy { MutableLiveData<Event<Boolean>>() }

    /* Navigation listener for taking a decision to whether switch a view to full-screen  */
    private val fullScreenPageIds = arrayOf(
        R.id.splashFragment,
        R.id.selectAccountFragment,
        R.id.scan,
        R.id.confirmFragment,
        R.id.feedbackFragment,
        R.id.loadingFragment
    )
    val fullScreenNavigatedListener: NavController.OnNavigatedListener by lazy {
        NavController.OnNavigatedListener { _, destination ->
            liveToolbarBottomNavVisibility.value = if (destination.id in fullScreenPageIds) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    /* Navigation listener for taking a decision to whether should show the next button on the toolbar */
    val showNextBtnDestinationIds = arrayOf(R.id.receive, R.id.topup)
    val nextButtonNavigatedListener: NavController.OnNavigatedListener by lazy {
        NavController.OnNavigatedListener { _, destination ->
            liveShowNext.value = destination.id in showNextBtnDestinationIds
        }
    }

    /* Navigation listener for set calculator mode when changing between receive and topup page */
    val calculatorModeNavigatedListener: NavController.OnNavigatedListener by lazy {
        NavController.OnNavigatedListener { _, destination ->
            currentCalculatorMode = when (destination.id) {
                R.id.receive -> CalculatorMode.RECEIVE
                R.id.topup -> CalculatorMode.TOPUP
                else -> currentCalculatorMode
            }
        }
    }

    fun createActionForScanPage(
        receiveViewModel: ReceiveViewModel,
        topupViewModel: TopupViewModel
    ): NavBottomNavigationDirections.ActionGlobalScanFragment {
        return when (currentCalculatorMode) {
            CalculatorMode.RECEIVE -> {
                NavBottomNavigationDirections.ActionGlobalScanFragment(receiveViewModel.liveSelectedToken.value!!)
                    .setAmount(receiveViewModel.liveCalculator.value!!)
                    .setTransactionType(SCAN_RECEIVE)
            }
            CalculatorMode.TOPUP -> {
                NavBottomNavigationDirections.ActionGlobalScanFragment(topupViewModel.liveSelectedToken.value!!)
                    .setAmount(topupViewModel.liveCalculator.value!!)
                    .setTransactionType(SCAN_TOPUP)
            }
        }
    }

    fun createActionForConfirmPage(
        receiveViewModel: ReceiveViewModel,
        topupViewModel: TopupViewModel
    ): NavBottomNavigationDirections.ActionGlobalConfirmFragment {
        return when (currentCalculatorMode) {
            CalculatorMode.RECEIVE -> {
                NavBottomNavigationDirections.ActionGlobalConfirmFragment(receiveViewModel.liveSelectedToken.value!!)
                    .setAmount(receiveViewModel.liveCalculator.value!!)
                    .setTransactionType(SCAN_RECEIVE)
            }
            CalculatorMode.TOPUP -> {
                NavBottomNavigationDirections.ActionGlobalConfirmFragment(topupViewModel.liveSelectedToken.value!!)
                    .setAmount(topupViewModel.liveCalculator.value!!)
                    .setTransactionType(SCAN_TOPUP)
            }
        }
    }

    fun createActionForFeedbackPage(feedback: Feedback): NavBottomNavigationDirections.ActionGlobalFeedbackFragment {
        return NavBottomNavigationDirections.ActionGlobalFeedbackFragment(feedback)
    }

    fun displayOtherDestinationByCondition() {
        when (meetDestination()) {
            DestinationCondition.DEST_SELECT_ACCOUNT -> {
                liveDestinationId.value = Event(R.id.action_global_selectAccountFragment)
            }
            DestinationCondition.DEST_SPLASH -> {
                liveDestinationId.value = Event(R.id.action_global_splashFragment)
                showSplash = false
            }
            DestinationCondition.DEST_MAIN -> {
            }
        }
    }

    /* Fetch data from repository */
    fun getAccount() = localRepository.loadAccount()

    fun getTokens() = liveTokenAPIResult.fetchedThenCache {
        remoteRepository.listTokens(TokenListParams.create(perPage = 30, searchTerm = null), liveTokenAPIResult)
        liveTokenAPIResult
    }

    internal fun meetDestination(): DestinationCondition {
        return when {
            getAccount() == null -> DestinationCondition.DEST_SELECT_ACCOUNT
            showSplash -> DestinationCondition.DEST_SPLASH
            else -> DestinationCondition.DEST_MAIN
        }
    }

    enum class DestinationCondition {
        DEST_SPLASH,
        DEST_SELECT_ACCOUNT,
        DEST_MAIN;
    }
}
