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
import network.omisego.omgmerchant.pages.account.SelectAccountRepository
import network.omisego.omgmerchant.pages.account.SelectAccountViewModel
import network.omisego.omgmerchant.pages.main.MainRepository
import network.omisego.omgmerchant.pages.main.MainViewModel
import network.omisego.omgmerchant.pages.main.TokenRepository
import network.omisego.omgmerchant.pages.main.WalletRepository
import network.omisego.omgmerchant.pages.main.more.account.SaveAccountViewModel
import network.omisego.omgmerchant.pages.main.more.account.SettingAccountRepository
import network.omisego.omgmerchant.pages.main.more.account.SettingAccountViewModel
import network.omisego.omgmerchant.pages.main.more.settinghelp.ConfirmFingerprintViewModel
import network.omisego.omgmerchant.pages.main.receive.ReceiveViewModel
import network.omisego.omgmerchant.pages.main.topup.TopupViewModel
import network.omisego.omgmerchant.pages.signin.SignInRepository

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
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
