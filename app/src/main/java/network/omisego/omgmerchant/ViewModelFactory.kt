package network.omisego.omgmerchant

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import network.omisego.omgmerchant.network.ParamsCreator
import network.omisego.omgmerchant.pages.authorized.NavDirectionCreator
import network.omisego.omgmerchant.pages.authorized.account.SelectAccountViewModel
import network.omisego.omgmerchant.pages.authorized.loading.LoadingViewModel
import network.omisego.omgmerchant.pages.authorized.main.MainViewModel
import network.omisego.omgmerchant.pages.authorized.main.more.account.SaveAccountViewModel
import network.omisego.omgmerchant.pages.authorized.main.more.account.SettingAccountViewModel
import network.omisego.omgmerchant.pages.authorized.main.more.settinghelp.ConfirmFingerprintViewModel
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(SelectAccountViewModel::class.java) -> {
                return SelectAccountViewModel(LocalRepository(), RemoteRepository()) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(
                    NavDirectionCreator(),
                    LocalRepository(),
                    RemoteRepository(),
                    ParamsCreator()
                ) as T
            }
            modelClass.isAssignableFrom(SettingAccountViewModel::class.java) -> {
                return SettingAccountViewModel(LocalRepository(), RemoteRepository(), ParamsCreator()) as T
            }
            modelClass.isAssignableFrom(SaveAccountViewModel::class.java) -> {
                return SaveAccountViewModel() as T
            }
            modelClass.isAssignableFrom(ConfirmFingerprintViewModel::class.java) -> {
                return ConfirmFingerprintViewModel(LocalRepository(), RemoteRepository(), ParamsCreator()) as T
            }
            modelClass.isAssignableFrom(LoadingViewModel::class.java) -> {
                return LoadingViewModel(RemoteRepository(), ParamsCreator()) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
