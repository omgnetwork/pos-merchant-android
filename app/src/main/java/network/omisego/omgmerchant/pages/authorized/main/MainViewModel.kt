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
import androidx.navigation.NavDirections
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.pagination.PaginationList
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.extensions.fetchedThenCache
import network.omisego.omgmerchant.extensions.logi
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.network.ParamsCreator
import network.omisego.omgmerchant.pages.authorized.MainNavDirectionCreator
import network.omisego.omgmerchant.pages.authorized.main.receive.ReceiveViewModel
import network.omisego.omgmerchant.pages.authorized.main.topup.TopupViewModel
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository

class MainViewModel(
    private val navDirectionCreator: MainNavDirectionCreator,
    internal val localRepository: LocalRepository,
    val remoteRepository: RemoteRepository,
    val paramsCreator: ParamsCreator = ParamsCreator()
) : ViewModel(), MainNavDirectionCreator by navDirectionCreator {
    internal var showSplash = true
    internal var currentCalculatorMode: CalculatorMode = CalculatorMode.RECEIVE

    /* LiveData */
    val liveTokenAPIResult: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }

    /* Control next button ui */
    val liveEnableNext: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val liveShowNext: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    /* Control whether should hide toolbar or bottom navigation e.g. the select account page has its own toolbar, so we don't need to show it. */
    val liveToolbarBottomNavVisibility: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    /* Control navigation to conditional destination e.g. the user hasn't load an account yet, should go to select account page. */
    val liveDirection: MutableLiveData<Event<NavDirections>> by lazy { MutableLiveData<Event<NavDirections>>() }

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

    fun displayOtherDestinationByCondition() {
        when (meetDestination()) {
            DestinationCondition.DEST_SELECT_ACCOUNT -> {
                liveDirection.value = Event(createDestinationSelectAccount())
            }
            DestinationCondition.DEST_SPLASH -> {
                liveDirection.value = Event(createDestinationSplash())
                showSplash = false
            }
            DestinationCondition.DEST_MAIN -> {
            }
        }
    }

    fun getAmountTokenPairByCalculatorMode(
        receiveViewModel: ReceiveViewModel,
        topupViewModel: TopupViewModel
    ): Pair<String, Token> {
        return when (currentCalculatorMode) {
            CalculatorMode.RECEIVE -> {
                receiveViewModel.liveCalculator.value!! to receiveViewModel.liveSelectedToken.value!!
            }
            CalculatorMode.TOPUP -> {
                topupViewModel.liveCalculator.value!! to topupViewModel.liveSelectedToken.value!!
            }
        }
    }

    fun handleLoadTokenSuccess(tokens: PaginationList<Token>) {
        logi("Loaded ${tokens.data.size} tokens")
    }

    /* Fetch data from repository */
    fun getAccount() = localRepository.loadAccount()

    fun getTokens() = liveTokenAPIResult.fetchedThenCache {
        remoteRepository.listTokens(paramsCreator.createListTokensParams(), liveTokenAPIResult)
        liveTokenAPIResult
    }

    fun clearSession() {
        localRepository.clearSession()
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
