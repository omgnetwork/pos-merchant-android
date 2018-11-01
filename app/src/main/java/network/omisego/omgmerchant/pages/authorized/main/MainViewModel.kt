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
import network.omisego.omgmerchant.data.LocalRepository
import network.omisego.omgmerchant.data.RemoteRepository
import network.omisego.omgmerchant.extensions.fetchedThenCache
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.pages.authorized.main.receive.ReceiveViewModel
import network.omisego.omgmerchant.pages.authorized.main.shared.spinner.LoadTokenViewModel
import network.omisego.omgmerchant.pages.authorized.main.topup.TopupViewModel
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_TOPUP

class MainViewModel(
    internal val localRepository: LocalRepository,
    val remoteRepository: RemoteRepository
) : ViewModel(), LoadTokenViewModel {
    private var showSplash = true
    private var currentCalculatorMode: CalculatorMode = CalculatorMode.RECEIVE

    /* LiveData */
    override val liveTokenAPIResult: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }

    /* Control next button ui */
    val liveEnableNext: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val liveShowNext: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    /* Control navigation to conditional destination e.g. the user hasn't load an account yet, should go to select account page. */
    val liveDestinationId: MutableLiveData<Event<Int>> by lazy { MutableLiveData<Event<Int>>() }

    /* Control whether should hide toolbar or bottom navigation e.g. the select account page has its own toolbar, so we don't need to show it. */
    val liveShowFullScreen: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    /* Navigation listener for taking a decision to whether switch a view to full-screen  */
    val fullScreenNavigatedListener: NavController.OnNavigatedListener by lazy {
        NavController.OnNavigatedListener { _, destination ->
            val fullScreenPageIds = arrayOf(R.id.splashFragment, R.id.selectAccountFragment, R.id.scan)
            liveShowFullScreen.value = if (destination.id in fullScreenPageIds) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    /* Navigation listener for taking a decision to whether should show the next button on the toolbar */
    val nextButtonNavigatedListener: NavController.OnNavigatedListener by lazy {
        NavController.OnNavigatedListener { _, destination ->
            val showNextBtnDestinationIds = arrayOf(R.id.receive, R.id.topup)
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

    /* Fetch data from repository */
    fun getAccount() = localRepository.getAccount()

    fun getCredential() = localRepository.loadCredential()

    fun getFeedback() = localRepository.loadFeedback()

    fun getTokens() = liveTokenAPIResult.fetchedThenCache {
        remoteRepository.listTokens(TokenListParams.create(perPage = 30, searchTerm = null), liveTokenAPIResult)
        liveTokenAPIResult
    }

    fun hasCredential() = localRepository.hasCredential()

    fun decideDestination() {
        when {
            getAccount() == null -> {
                liveDestinationId.value = Event(R.id.action_global_selectAccountFragment)
            }
            showSplash -> {
                liveDestinationId.value = Event(R.id.action_global_splashFragment)
                showSplash = false
            }
        }
    }

    fun createActionForScanPage(
        receiveViewModel: ReceiveViewModel,
        topupViewModel: TopupViewModel
    ): NavBottomNavigationDirections.ActionGlobalScanFragment {
        return when (currentCalculatorMode) {
            CalculatorMode.RECEIVE -> {
                NavBottomNavigationDirections.ActionGlobalScanFragment(receiveViewModel.liveToken.value!!)
                    .setAmount(receiveViewModel.liveCalculator.value!!)
                    .setTransactionType(SCAN_RECEIVE)
            }
            CalculatorMode.TOPUP -> {
                NavBottomNavigationDirections.ActionGlobalScanFragment(topupViewModel.liveToken.value!!)
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
                NavBottomNavigationDirections.ActionGlobalConfirmFragment(receiveViewModel.liveToken.value!!)
                    .setAmount(receiveViewModel.liveCalculator.value!!)
                    .setTransactionType(SCAN_RECEIVE)
            }
            CalculatorMode.TOPUP -> {
                NavBottomNavigationDirections.ActionGlobalConfirmFragment(topupViewModel.liveToken.value!!)
                    .setAmount(topupViewModel.liveCalculator.value!!)
                    .setTransactionType(SCAN_TOPUP)
            }
        }
    }

//    fun createActionForFeedbackPage(): MainFragmentDirections.ActionMainToFeedback {
//        return MainFragmentDirections.ActionMainToFeedback(getFeedback()!!)
//    }

}
