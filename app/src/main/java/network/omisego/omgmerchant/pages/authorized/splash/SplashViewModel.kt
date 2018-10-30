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
import co.omisego.omisego.model.params.AccountWalletListParams
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.data.LocalRepository
import network.omisego.omgmerchant.data.RemoteRepository
import network.omisego.omgmerchant.extensions.mutableLiveDataOf

class SplashViewModel(
    private val app: Application,
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : AndroidViewModel(app) {
    val liveAccount: MutableLiveData<Account> by lazy { mutableLiveDataOf<Account>() }
    val accountDescription: String
        get() = app.getString(R.string.welcome_account_info, liveAccount.value?.name)

    fun loadAccount(): Account? {
        liveAccount.value = localRepository.getAccount()
        return liveAccount.value
    }

    fun loadWalletAndSave() {
        remoteRepository.loadWalletAndSave(AccountWalletListParams.create(id = localRepository.getAccount()!!.id, searchTerm = null))
    }
}
