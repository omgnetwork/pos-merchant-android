package network.omisego.omgmerchant.pages.main

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 15/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.params.AccountWalletListParams
import co.omisego.omisego.model.params.TokenListParams
import network.omisego.omgmerchant.extensions.mutableLiveDataOf
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.storage.Storage

class MainViewModel(
    private val tokenRepository: TokenRepository,
    private val walletRepository: WalletRepository
) : ViewModel() {
    val liveEnableNext: MutableLiveData<Boolean> by lazy { mutableLiveDataOf(false) }
    val liveTokenAPIResult: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }
    val liveWalletAPIResult: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }

    fun getTokens(): LiveData<APIResult> {
        return if (liveTokenAPIResult.value == null)
            return tokenRepository.listTokens(TokenListParams.create(perPage = 30, searchTerm = null), liveTokenAPIResult)
        else
            liveTokenAPIResult
    }

    fun loadWallet(): LiveData<APIResult> {
        val account = Storage.loadAccount()!!
        return if (liveWalletAPIResult.value == null)
            walletRepository.loadWallet(
                AccountWalletListParams.create(id = account.id, searchTerm = null),
                liveWalletAPIResult
            )
        else
            liveWalletAPIResult
    }
}
