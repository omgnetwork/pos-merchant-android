package network.omisego

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import network.omisego.omgmerchant.calculator.Calculation
import network.omisego.omgmerchant.calculator.CalculatorHandler
import network.omisego.omgmerchant.model.LiveCalculator
import network.omisego.omgmerchant.pages.account.SelectAccountRepository
import network.omisego.omgmerchant.pages.account.SelectAccountViewModel
import network.omisego.omgmerchant.pages.main.MainViewModel
import network.omisego.omgmerchant.pages.main.TokenRepository
import network.omisego.omgmerchant.pages.main.WalletRepository
import network.omisego.omgmerchant.pages.main.receive.ReceiveViewModel
import network.omisego.omgmerchant.pages.main.topup.TopupViewModel
import network.omisego.omgmerchant.pages.scan.ScanRepository
import network.omisego.omgmerchant.pages.scan.ScanViewModel
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
            modelClass.isAssignableFrom(ReceiveViewModel::class.java) -> {
                return ReceiveViewModel(
                    CalculatorHandler(),
                    LiveCalculator("0"),
                    Calculation()
                ) as T
            }
            modelClass.isAssignableFrom(TopupViewModel::class.java) -> {
                return TopupViewModel(
                    CalculatorHandler(),
                    LiveCalculator("0")
                ) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(
                    TokenRepository(),
                    WalletRepository()
                ) as T
            }
            modelClass.isAssignableFrom(ScanViewModel::class.java) -> {
                return ScanViewModel(
                    ScanRepository()
                ) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
