package network.omisego.omgmerchant

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import network.omisego.omgmerchant.calculator.Calculation
import network.omisego.omgmerchant.calculator.CalculatorInteraction
import network.omisego.omgmerchant.helper.HelperAmountFormatter
import network.omisego.omgmerchant.pages.authorized.confirm.ConfirmViewModel
import network.omisego.omgmerchant.pages.authorized.feedback.FeedbackTransformer
import network.omisego.omgmerchant.pages.authorized.feedback.FeedbackViewModel
import network.omisego.omgmerchant.pages.authorized.main.more.setting.SettingViewModel
import network.omisego.omgmerchant.pages.authorized.main.more.settinghelp.SettingHelpViewModel
import network.omisego.omgmerchant.pages.authorized.main.more.transaction.TransactionListTransformer
import network.omisego.omgmerchant.pages.authorized.main.more.transaction.TransactionListViewModel
import network.omisego.omgmerchant.pages.authorized.main.receive.ReceiveViewModel
import network.omisego.omgmerchant.pages.authorized.main.topup.TopupViewModel
import network.omisego.omgmerchant.pages.authorized.scan.ScanViewModel
import network.omisego.omgmerchant.pages.authorized.splash.SplashViewModel
import network.omisego.omgmerchant.pages.unauthorized.signin.FingerprintBottomSheetViewModel
import network.omisego.omgmerchant.pages.unauthorized.signin.SignInViewModel
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

@Suppress("UNCHECKED_CAST")
class AndroidViewModelFactory(private val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                return SignInViewModel(application, LocalRepository(), RemoteRepository()) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(application, LocalRepository(), RemoteRepository()) as T
            }
            modelClass.isAssignableFrom(FeedbackViewModel::class.java) -> {
                FeedbackViewModel(application, FeedbackTransformer()) as T
            }
            modelClass.isAssignableFrom(ScanViewModel::class.java) -> {
                ScanViewModel(application, HelperAmountFormatter(), RemoteRepository()) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(application, LocalRepository()) as T
            }
            modelClass.isAssignableFrom(TransactionListViewModel::class.java) -> {
                TransactionListViewModel(application, LocalRepository(), RemoteRepository(), TransactionListTransformer(application)) as T
            }
            modelClass.isAssignableFrom(SettingHelpViewModel::class.java) -> {
                SettingHelpViewModel(application, LocalRepository()) as T
            }
            modelClass.isAssignableFrom(FingerprintBottomSheetViewModel::class.java) -> {
                FingerprintBottomSheetViewModel(application) as T
            }
            modelClass.isAssignableFrom(ConfirmViewModel::class.java) -> {
                ConfirmViewModel(application, LocalRepository(), RemoteRepository()) as T
            }
            modelClass.isAssignableFrom(ReceiveViewModel::class.java) -> {
                return ReceiveViewModel(
                    application,
                    CalculatorInteraction(),
                    Calculation()
                ) as T
            }

            modelClass.isAssignableFrom(TopupViewModel::class.java) -> {
                return TopupViewModel(
                    application,
                    CalculatorInteraction()
                ) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
