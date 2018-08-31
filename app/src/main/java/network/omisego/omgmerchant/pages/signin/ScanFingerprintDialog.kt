package network.omisego.omgmerchant.pages.signin

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
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

class ScanFingerprintDialog : BottomSheetDialogFragment() {
    var liveConfirmSuccess: MutableLiveData<Boolean>? = MutableLiveData()
    val goldFinger: Goldfinger by lazy { Goldfinger.Builder(context).build() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.bottom_sheet_fingerprint_scan, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnCancel.setOnClickListener {
            dismiss()
        }

        if (goldFinger.hasEnrolledFingerprint()) {
            goldFinger.authenticate(object : Goldfinger.Callback() {
                override fun onSuccess(value: String?) {
                    liveConfirmSuccess?.value = true
                    dismiss()
                }

                override fun onError(error: Error?) {
                    if (error?.isCritical == true) {
                        liveConfirmSuccess?.value = false
                        dismiss()
                    } else {
                        // show an error icon
                        ivFingerprint.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_error))
                        tvDescription.text = getString(R.string.dialog_fingerprint_status_error)
                    }
                }
            })
        } else {
            toast(getString(R.string.dialog_fingerprint_unsupported))
            dismiss()
        }
    }
}
