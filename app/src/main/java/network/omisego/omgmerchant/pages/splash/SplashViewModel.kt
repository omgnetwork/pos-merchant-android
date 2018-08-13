package network.omisego.omgmerchant.pages.splash

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.Account
import network.omisego.omgmerchant.extensions.mutableLiveDataOf

class SplashViewModel(
    private val splashRepository: SplashRepository
) : ViewModel() {
    val liveAccount: MutableLiveData<Account> by lazy { mutableLiveDataOf<Account>() }

    fun loadAccount() = splashRepository.loadAccount()
}
