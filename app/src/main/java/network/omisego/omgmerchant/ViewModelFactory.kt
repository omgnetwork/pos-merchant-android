package network.omisego.omgmerchant

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository
import network.omisego.omgmerchant.pages.authorized.account.SelectAccountViewModel
import network.omisego.omgmerchant.pages.authorized.confirm.handler.ConsumeTransactionRequestHandlerViewModel
import network.omisego.omgmerchant.pages.authorized.confirm.handler.CreateTransactionHandlerViewModel
import network.omisego.omgmerchant.pages.authorized.main.MainViewModel
import network.omisego.omgmerchant.pages.authorized.main.more.account.SaveAccountViewModel
import network.omisego.omgmerchant.pages.authorized.main.more.account.SettingAccountViewModel
import network.omisego.omgmerchant.pages.authorized.main.more.settinghelp.ConfirmFingerprintViewModel
import network.omisego.omgmerchant.pages.authorized.scan.QRPayloadViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(SelectAccountViewModel::class.java) -> {
                return SelectAccountViewModel(LocalRepository(), RemoteRepository()) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(
                    LocalRepository(),
                    RemoteRepository()
                ) as T
            }
            modelClass.isAssignableFrom(SettingAccountViewModel::class.java) -> {
                return SettingAccountViewModel(LocalRepository(), RemoteRepository()) as T
            }
            modelClass.isAssignableFrom(SaveAccountViewModel::class.java) -> {
                return SaveAccountViewModel() as T
            }
            modelClass.isAssignableFrom(ConfirmFingerprintViewModel::class.java) -> {
                return ConfirmFingerprintViewModel(LocalRepository(), RemoteRepository()) as T
            }
            modelClass.isAssignableFrom(QRPayloadViewModel::class.java) -> {
                return QRPayloadViewModel() as T
            }
            modelClass.isAssignableFrom(CreateTransactionHandlerViewModel::class.java) -> {
                return CreateTransactionHandlerViewModel(LocalRepository(), RemoteRepository()) as T
            }
            modelClass.isAssignableFrom(ConsumeTransactionRequestHandlerViewModel::class.java) -> {
                return ConsumeTransactionRequestHandlerViewModel(LocalRepository(), RemoteRepository()) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
