package network.omisego.omgmerchant.pages.main

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 15/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.params.AccountWalletListParams
import co.omisego.omisego.model.params.TokenListParams
import network.omisego.omgmerchant.extensions.fetchedThenCache
import network.omisego.omgmerchant.extensions.mutableLiveDataOf
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.LiveCalculator
import network.omisego.omgmerchant.pages.main.receive.ReceiveViewModel
import network.omisego.omgmerchant.pages.main.shared.spinner.LoadTokenViewModel
import network.omisego.omgmerchant.pages.main.topup.TopupViewModel
import network.omisego.omgmerchant.pages.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.pages.scan.SCAN_TOPUP

class MainViewModel(
    private val tokenRepository: TokenRepository,
    private val walletRepository: WalletRepository,
    private val mainRepository: MainRepository
) : ViewModel(), LoadTokenViewModel {
    override val liveTokenAPIResult: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }
    val liveEnableNext: MutableLiveData<Boolean> by lazy { mutableLiveDataOf(false) }

    fun getAccount() = mainRepository.getAccount()

    fun getCredential() = mainRepository.getCredential()

    fun getFeedback() = mainRepository.getFeedback()

    fun getTokens() = liveTokenAPIResult.fetchedThenCache {
        tokenRepository.listTokens(TokenListParams.create(perPage = 30, searchTerm = null), liveTokenAPIResult)
    }

    fun loadWalletAndSave() {
        walletRepository.loadWalletAndSave(AccountWalletListParams.create(id = mainRepository.getAccount()!!.id, searchTerm = null))
    }

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

    fun createActionForScanPage(
        receiveViewModel: ReceiveViewModel,
        topupViewModel: TopupViewModel,
        page: Int
    ): MainFragmentDirections.ActionMainToScan {
        return when (page) {
            PAGE_RECEIVE -> {
                MainFragmentDirections.ActionMainToScan(receiveViewModel.liveToken.value!!)
                    .setAmount(receiveViewModel.liveCalculator.value!!)
                    .setTransactionType(SCAN_RECEIVE)
            }
            PAGE_TOPUP -> {
                MainFragmentDirections.ActionMainToScan(topupViewModel.liveToken.value!!)
                    .setAmount(topupViewModel.liveCalculator.value!!)
                    .setTransactionType(SCAN_TOPUP)
            }
            else -> {
                throw IllegalStateException("Page $page doesn't currently support.")
            }
        }
    }
}
