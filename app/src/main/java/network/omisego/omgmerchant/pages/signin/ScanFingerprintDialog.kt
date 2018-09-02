package network.omisego.omgmerchant.pages.signin

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.infinum.goldfinger.Error
import co.infinum.goldfinger.Goldfinger
import kotlinx.android.synthetic.main.bottom_sheet_fingerprint_scan.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.extensions.toast
import network.omisego.omgmerchant.utils.FingerprintHelper

class ScanFingerprintDialog : BottomSheetDialogFragment() {
    var liveConfirmSuccess: MutableLiveData<Boolean>? = MutableLiveData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.bottom_sheet_fingerprint_scan, container)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        FingerprintHelper.cancel()
        super.onDismiss(dialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnCancel.setOnClickListener {
            dismiss()
        }

        if (FingerprintHelper.hasEnrolledFingerprint()) {
            FingerprintHelper.authenticate(object : Goldfinger.Callback() {
                override fun onSuccess(value: String?) {
                    liveConfirmSuccess?.value = true
                    dismiss()
                }

                override fun onError(error: Error?) {
                    if (error?.isCritical == true) {
                        when (error) {
                            Error.LOCKOUT -> {
                                tvDescription.text = getText(R.string.dialog_fingerprint_error_too_many_attempt)
                            }
                            else -> {
                                tvDescription.text = getText(R.string.dialog_fingerprint_error_unable_to_process)
                            }
                        }
                        liveConfirmSuccess?.value = false
                    } else {
                        when (error) {
                            Error.TOO_FAST -> {
                                tvDescription.text = getText(R.string.dialog_fingerprint_error_too_fast)
                            }
                            Error.TOO_SLOW -> {
                                tvDescription.text = getText(R.string.dialog_fingerprint_error_too_slow)
                            }
                            else -> {
                                tvDescription.text = getString(R.string.dialog_fingerprint_error_try_again)
                            }
                        }
                    }
                    ivFingerprint.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_error))
                }
            })
        } else {
            toast(getString(R.string.dialog_fingerprint_unsupported))
            dismiss()
        }
    }
}
