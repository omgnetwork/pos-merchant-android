package network.omisego.omgmerchant.pages.authorized.scan

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.qrcode.scanner.OMGQRScannerContract
import co.omisego.omisego.qrcode.scanner.SimpleVerifierListener

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class AddressViewModel : ViewModel(), SimpleVerifierListener {
    override fun onCanceled(scannerView: OMGQRScannerContract.View) {
    }

    override fun onDecoded(scannerView: OMGQRScannerContract.View, payload: String) {
        liveAddress.value = payload
    }

    val liveAddress: MutableLiveData<String> by lazy { MutableLiveData<String>() }
}