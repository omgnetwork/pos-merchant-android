package network.omisego.omgmerchant.pages.signin

import android.arch.lifecycle.MutableLiveData
import android.hardware.biometrics.BiometricPrompt
import android.os.Build.VERSION_CODES.P
import android.support.annotation.RequiresApi

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

@RequiresApi(P)
class BiometricCallback(
    private val liveAuthenticationError: MutableLiveData<Pair<Int, CharSequence?>>,
    private val liveAuthenticationSucceeded: MutableLiveData<BiometricPrompt.CryptoObject>,
    private val liveAuthenticationFailed: MutableLiveData<Unit>,
    private val liveAuthenticationHelp: MutableLiveData<Pair<Int, CharSequence?>>
) : BiometricPrompt.AuthenticationCallback() {
    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
        liveAuthenticationError.value = errorCode to errString
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        liveAuthenticationSucceeded.value = result.cryptoObject
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
        liveAuthenticationHelp.value = helpCode to helpString
    }

    override fun onAuthenticationFailed() {
        liveAuthenticationFailed.value = null
    }
}
