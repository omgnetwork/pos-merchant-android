package network.omisego.omgmerchant

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import network.omisego.omgmerchant.calculator.Calculation
import network.omisego.omgmerchant.calculator.CalculatorInteraction
import network.omisego.omgmerchant.data.LocalRepository
import network.omisego.omgmerchant.data.RemoteRepository
import network.omisego.omgmerchant.model.LiveCalculator
import network.omisego.omgmerchant.pages.authorized.account.SelectAccountViewModel
import network.omisego.omgmerchant.pages.authorized.main.MainViewModel
import network.omisego.omgmerchant.pages.authorized.main.more.account.SaveAccountViewModel
import network.omisego.omgmerchant.pages.authorized.main.more.account.SettingAccountViewModel
import network.omisego.omgmerchant.pages.authorized.main.more.settinghelp.ConfirmFingerprintViewModel
import network.omisego.omgmerchant.pages.authorized.main.receive.ReceiveViewModel
import network.omisego.omgmerchant.pages.authorized.main.topup.TopupViewModel
import network.omisego.omgmerchant.pages.authorized.scan.AddressViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(SelectAccountViewModel::class.java) -> {
                return SelectAccountViewModel(LocalRepository(), RemoteRepository()) as T
            }
            modelClass.isAssignableFrom(ReceiveViewModel::class.java) -> {
                return ReceiveViewModel(
                    CalculatorInteraction(),
                    LiveCalculator("0"),
                    Calculation()
                ) as T
            }
            modelClass.isAssignableFrom(TopupViewModel::class.java) -> {
                return TopupViewModel(
                    CalculatorInteraction(),
                    LiveCalculator("0")
                ) as T
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
            modelClass.isAssignableFrom(AddressViewModel::class.java) -> {
                return AddressViewModel() as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
