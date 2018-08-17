package network.omisego.omgmerchant.pages.main

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 15/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import co.omisego.omisego.model.params.AccountWalletListParams
import co.omisego.omisego.model.params.TokenListParams
import network.omisego.omgmerchant.extensions.fetchedThenCache
import network.omisego.omgmerchant.extensions.mutableLiveDataOf
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.LiveCalculator
import network.omisego.omgmerchant.pages.main.receive.ReceiveViewModel
import network.omisego.omgmerchant.pages.main.shared.spinner.LoadTokenViewModel
import network.omisego.omgmerchant.pages.main.topup.TopupViewModel

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

    fun createBundleForScanPage(
        receiveViewModel: ReceiveViewModel,
        topupViewModel: TopupViewModel,
        page: Int
    ): Bundle {
        val bundle = Bundle()
        when (page) {
            PAGE_RECEIVE -> {
                bundle.putString("transaction_type", "receive")
                bundle.putDouble("amount", receiveViewModel.liveCalculator.value!!.toDouble())
                bundle.putParcelable("token", receiveViewModel.liveToken.value!!)
            }
            PAGE_TOPUP -> {
                bundle.putString("transaction_type", "topup")
                bundle.putDouble("amount", topupViewModel.liveCalculator.value!!.toDouble())
                bundle.putParcelable("token", topupViewModel.liveToken.value!!)
            }
        }
        return bundle
    }
}
