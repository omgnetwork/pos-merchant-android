package network.omisego

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import network.omisego.omgmerchant.pages.account.SelectAccountRepository
import network.omisego.omgmerchant.pages.account.SelectAccountViewModel
import network.omisego.omgmerchant.pages.signin.SignInRepository
import network.omisego.omgmerchant.pages.signin.SignInViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                return SignInViewModel(SignInRepository()) as T
            }
            modelClass.isAssignableFrom(SelectAccountViewModel::class.java) -> {
                return SelectAccountViewModel(SelectAccountRepository()) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}