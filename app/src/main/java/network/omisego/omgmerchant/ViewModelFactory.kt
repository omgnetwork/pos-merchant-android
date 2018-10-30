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
import network.omisego.omgmerchant.model.LiveCalculator
import network.omisego.omgmerchant.pages.authorized.account.SelectAccountRepository
import network.omisego.omgmerchant.pages.authorized.account.SelectAccountViewModel
import network.omisego.omgmerchant.pages.authorized.main.MainRepository
import network.omisego.omgmerchant.pages.authorized.main.MainViewModel
import network.omisego.omgmerchant.pages.authorized.main.TokenRepository
import network.omisego.omgmerchant.pages.authorized.main.WalletRepository
import network.omisego.omgmerchant.pages.authorized.main.more.account.SaveAccountViewModel
import network.omisego.omgmerchant.pages.authorized.main.more.account.SettingAccountRepository
import network.omisego.omgmerchant.pages.authorized.main.more.account.SettingAccountViewModel
import network.omisego.omgmerchant.pages.authorized.main.more.settinghelp.ConfirmFingerprintViewModel
import network.omisego.omgmerchant.pages.authorized.main.receive.ReceiveViewModel
import network.omisego.omgmerchant.pages.authorized.main.topup.TopupViewModel
import network.omisego.omgmerchant.pages.authorized.scan.AddressViewModel
import network.omisego.omgmerchant.pages.unauthorized.signin.SignInRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(SelectAccountViewModel::class.java) -> {
                return SelectAccountViewModel(SelectAccountRepository()) as T
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
                    TokenRepository(),
                    WalletRepository(),
                    MainRepository()
                ) as T
            }
            modelClass.isAssignableFrom(SettingAccountViewModel::class.java) -> {
                return SettingAccountViewModel(SettingAccountRepository(), WalletRepository()) as T
            }
            modelClass.isAssignableFrom(SaveAccountViewModel::class.java) -> {
                return SaveAccountViewModel(SettingAccountRepository()) as T
            }
            modelClass.isAssignableFrom(ConfirmFingerprintViewModel::class.java) -> {
                return ConfirmFingerprintViewModel(SignInRepository()) as T
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
