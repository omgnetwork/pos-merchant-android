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
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.data.LocalRepository
import network.omisego.omgmerchant.data.RemoteRepository
import network.omisego.omgmerchant.extensions.fetchedThenCache
import network.omisego.omgmerchant.extensions.mutableLiveDataOf
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.LiveCalculator
import network.omisego.omgmerchant.pages.authorized.main.shared.spinner.LoadTokenViewModel

class MainViewModel(
    internal val localRepository: LocalRepository,
    val remoteRepository: RemoteRepository
) : ViewModel(), LoadTokenViewModel {
    private var showSplash = true

    /* LiveData */
    override val liveTokenAPIResult: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }
    val liveEnableNext: MutableLiveData<Boolean> by lazy { mutableLiveDataOf(false) }
    val livePage: MutableLiveData<Int> by lazy { mutableLiveDataOf(PAGE_RECEIVE) }
    val liveDestinationId: MutableLiveData<Event<Int>> by lazy { MutableLiveData<Event<Int>>() }
    val liveShowFullScreen: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    /* Navigation listener for taking a decision to whether switch a view to full-screen  */
    val fullScreenNavigatedListener: NavController.OnNavigatedListener by lazy {
        NavController.OnNavigatedListener { _, destination ->
            val fullScreenPageIds = arrayOf(R.id.splashFragment, R.id.selectAccountFragment)
            liveShowFullScreen.value = if (destination.id in fullScreenPageIds) {
                View.GONE
            } else {
                View.VISIBLE
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

    fun handleEnableNextButtonByPager(liveCalculator: LiveCalculator, page: Int) {
        when (page) {
            PAGE_RECEIVE -> {
                liveEnableNext.value = liveCalculator.value != "0" &&
                    liveCalculator.value?.indexOfAny(charArrayOf('-', '+')) == -1
            }
            PAGE_TOPUP -> liveEnableNext.value = liveCalculator.value != "0"
            PAGE_MORE -> liveEnableNext.value = false
        }
    }

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
//    fun createActionForScanPage(
//        receiveViewModel: ReceiveViewModel,
//        topupViewModel: TopupViewModel
//    ): MainFragmentDirections.ActionMainToScan {
//        return when (livePage.value) {
//            PAGE_RECEIVE -> {
//                MainFragmentDirections.ActionMainToScan(receiveViewModel.liveToken.value!!)
//                    .setAmount(receiveViewModel.liveCalculator.value!!)
//                    .setTransactionType(SCAN_RECEIVE)
//            }
//            PAGE_TOPUP -> {
//                MainFragmentDirections.ActionMainToScan(topupViewModel.liveToken.value!!)
//                    .setAmount(topupViewModel.liveCalculator.value!!)
//                    .setTransactionType(SCAN_TOPUP)
//            }
//            else -> {
//                throw IllegalStateException("Page ${livePage.value} doesn't currently support.")
//            }
//        }
//    }
//
//    fun createActionForConfirmPage(
//        receiveViewModel: ReceiveViewModel,
//        topupViewModel: TopupViewModel
//    ): MainFragmentDirections.ActionMainToConfirm {
//        return when (livePage.value) {
//            PAGE_RECEIVE -> {
//                MainFragmentDirections.ActionMainToConfirm(receiveViewModel.liveToken.value!!)
//                    .setAmount(receiveViewModel.liveCalculator.value!!)
//                    .setTransactionType(SCAN_RECEIVE)
//            }
//            PAGE_TOPUP -> {
//                MainFragmentDirections.ActionMainToConfirm(topupViewModel.liveToken.value!!)
//                    .setAmount(topupViewModel.liveCalculator.value!!)
//                    .setTransactionType(SCAN_TOPUP)
//            }
//            else -> {
//                throw IllegalStateException("Page ${livePage.value} doesn't currently support.")
//            }
//        }
//    }
//
//    fun createActionForFeedbackPage(): MainFragmentDirections.ActionMainToFeedback {
//        return MainFragmentDirections.ActionMainToFeedback(getFeedback()!!)
//    }

}
