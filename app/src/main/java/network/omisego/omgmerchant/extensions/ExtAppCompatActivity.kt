package network.omisego.omgmerchant.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import network.omisego.omgmerchant.AndroidViewModelFactory
import network.omisego.omgmerchant.MainActivity
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.ViewModelFactory

inline fun <reified T : ViewModel> AppCompatActivity.provideViewModel(): T {
    return ViewModelProviders.of(this, ViewModelFactory())[T::class.java]
}

inline fun <reified T : AndroidViewModel> AppCompatActivity.provideAndroidViewModel(): T {
    return ViewModelProviders.of(this, AndroidViewModelFactory(application))[T::class.java]
}

fun AppCompatActivity.scrollBottom() {
    if (this is MainActivity) {
        val scroller = this.findViewById<NestedScrollView>(R.id.nestedScrollView)
        scroller?.scrollTo(0, scroller.bottom)
    }
}

fun Fragment.scrollBottom() {
    val hostActivity = activity
    if (hostActivity is AppCompatActivity) {
        hostActivity.scrollBottom()
    }
}
