package network.omisego.omgmerchant.pages.authorized.splash

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import co.omisego.omisego.model.Account
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.network.ParamsCreator
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository

class SplashViewModel(
    private val app: Application,
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val paramsCreator: ParamsCreator = ParamsCreator()
) : AndroidViewModel(app) {
    val liveAccount: MutableLiveData<Account> by lazy { MutableLiveData<Account>() }
    val accountDescription: String
        get() = app.getString(R.string.welcome_account_info, liveAccount.value?.name)

    fun loadAccount(): Account? {
        liveAccount.value = localRepository.loadAccount()
        return liveAccount.value
    }

    fun loadWalletAndSave() {
        val params = paramsCreator.createAccountWalletListParams(localRepository.loadAccount()!!.id, searchTerm = null)
        remoteRepository.loadWalletAndSave(params)
    }
}
