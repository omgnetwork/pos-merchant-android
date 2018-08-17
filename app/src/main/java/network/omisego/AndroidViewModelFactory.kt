package network.omisego

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import network.omisego.omgmerchant.pages.feedback.FeedbackRepository
import network.omisego.omgmerchant.pages.feedback.FeedbackViewModel
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
        when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                return SplashViewModel(application, SplashRepository()) as T
            }
            modelClass.isAssignableFrom(FeedbackViewModel::class.java) -> {
                return FeedbackViewModel(application, FeedbackRepository()) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
