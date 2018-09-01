package network.omisego.omgmerchant.utils

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 1/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Context
import co.infinum.goldfinger.Goldfinger

object FingerprintHelper {
    private lateinit var goldFinger: Goldfinger

    fun init(context: Context) {
        goldFinger = Goldfinger.Builder(context).build()
    }

    fun hasEnrolledFingerprint() = goldFinger.hasEnrolledFingerprint()

    fun hasFingerprintHardware() = goldFinger.hasFingerprintHardware()

    fun authenticate(callback: Goldfinger.Callback) {
        goldFinger.authenticate(callback)
    }
}
