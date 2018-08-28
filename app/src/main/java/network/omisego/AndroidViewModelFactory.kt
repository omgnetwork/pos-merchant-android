package network.omisego

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import network.omisego.omgmerchant.pages.feedback.FeedbackRepository
import network.omisego.omgmerchant.pages.feedback.FeedbackViewModel
import network.omisego.omgmerchant.pages.main.ToolbarViewModel
import network.omisego.omgmerchant.pages.main.more.MoreViewModel
import network.omisego.omgmerchant.pages.main.more.setting.SettingViewModel
import network.omisego.omgmerchant.pages.main.more.settinghelp.SettingHelpViewModel
import network.omisego.omgmerchant.pages.main.more.transaction.TransactionListRepository
import network.omisego.omgmerchant.pages.main.more.transaction.TransactionListViewModel
import network.omisego.omgmerchant.pages.scan.ScanRepository
import network.omisego.omgmerchant.pages.scan.ScanViewModel
import network.omisego.omgmerchant.pages.splash.SplashRepository
import network.omisego.omgmerchant.pages.splash.SplashViewModel

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
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(application, SplashRepository()) as T
            }
            modelClass.isAssignableFrom(FeedbackViewModel::class.java) -> {
                FeedbackViewModel(application, FeedbackRepository()) as T
            }
            modelClass.isAssignableFrom(ScanViewModel::class.java) -> {
                ScanViewModel(application, ScanRepository()) as T
            }
            modelClass.isAssignableFrom(MoreViewModel::class.java) -> {
                MoreViewModel(application) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(application) as T
            }
            modelClass.isAssignableFrom(TransactionListViewModel::class.java) -> {
                TransactionListViewModel(application, TransactionListRepository()) as T
            }
            modelClass.isAssignableFrom(ToolbarViewModel::class.java) -> {
                ToolbarViewModel(application) as T
            }
            modelClass.isAssignableFrom(SettingHelpViewModel::class.java) -> {
                SettingHelpViewModel(application) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
