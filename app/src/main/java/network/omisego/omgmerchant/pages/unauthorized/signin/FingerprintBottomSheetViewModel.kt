package network.omisego.omgmerchant.pages.unauthorized.signin

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 2/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import co.infinum.goldfinger.Error
import co.infinum.goldfinger.Goldfinger
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.helper.HelperContext

class FingerprintBottomSheetViewModel(
    val app: Application
) : AndroidViewModel(app) {
    private var goldFinger: Goldfinger? = null
    var liveAuthPass: MutableLiveData<Boolean> = MutableLiveData()
    var liveFingerprintIcon: MutableLiveData<Drawable> = MutableLiveData()
    var liveFingerprintDescription: MutableLiveData<String> = MutableLiveData()

    fun init(context: Context) {
        goldFinger = Goldfinger.Builder(context).build()
    }

    fun init() {
        goldFinger = Goldfinger.Builder(HelperContext.context).build()
    }

    fun hasEnrolledFingerprint() = goldFinger?.hasEnrolledFingerprint()

    fun hasFingerprintHardware() = goldFinger?.hasFingerprintHardware()

    fun authenticate() {
        when {
            hasEnrolledFingerprint() == false -> {
                liveFingerprintIcon.value = ContextCompat.getDrawable(app, R.drawable.ic_error)
                liveFingerprintDescription.value = app.getString(R.string.dialog_fingerprint_not_enrolled)
            }
            hasFingerprintHardware() == false -> {
                liveFingerprintIcon.value = ContextCompat.getDrawable(app, R.drawable.ic_error)
                liveFingerprintDescription.value = app.getString(R.string.dialog_fingerprint_unsupported)
            }
            else -> {
                liveFingerprintIcon.value = ContextCompat.getDrawable(app, R.drawable.ic_fingerprint)
                liveFingerprintDescription.value = app.getString(R.string.dialog_fingerprint_status_normal)
                goldFinger?.authenticate(object : Goldfinger.Callback() {
                    override fun onSuccess(value: String?) {
                        liveAuthPass.value = true
                    }

                    override fun onError(error: Error?) {
                        if (error?.isCritical == true) {
                            liveFingerprintDescription.value = when (error) {
                                Error.LOCKOUT -> {
                                    app.getString(R.string.dialog_fingerprint_error_too_many_attempt)
                                }
                                else -> {
                                    app.getString(R.string.dialog_fingerprint_error_unable_to_process)
                                }
                            }
                            liveAuthPass.value = false
                        } else {
                            liveFingerprintDescription.value = when (error) {
                                Error.TOO_FAST -> {
                                    app.getString(R.string.dialog_fingerprint_error_too_fast)
                                }
                                Error.TOO_SLOW -> {
                                    app.getString(R.string.dialog_fingerprint_error_too_slow)
                                }
                                else -> {
                                    app.getString(R.string.dialog_fingerprint_error_try_again)
                                }
                            }
                        }
                        liveFingerprintIcon.value = ContextCompat.getDrawable(app, R.drawable.ic_error)
                    }
                })
            }
        }
    }

    fun cancel() {
        goldFinger?.cancel()
    }
}