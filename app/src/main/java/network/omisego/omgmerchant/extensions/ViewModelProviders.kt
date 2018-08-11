package network.omisego.omgmerchant.extensions

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import network.omisego.ViewModelFactory

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

inline fun <reified T : ViewModel> Fragment.provideViewModel(): T {
    return ViewModelProviders.of(this, ViewModelFactory())[T::class.java]
}
