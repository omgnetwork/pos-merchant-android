package network.omisego.omgmerchant.pages.authorized.scan

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.qrcode.scanner.OMGQRScannerContract

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class AddressViewModel : ViewModel(), OMGQRScannerContract.Preview.Verifier {
    override var postVerification: OMGQRScannerContract.Preview.PostVerification? = null

    override fun onCanceled() {}

    override fun onDecoded(payload: String) {
        liveAddress.value = payload
    }

    fun removeCache(text: String?) {
        text ?: return
        postVerification?.onRemoveCache(cacheText = text)
    }

    val liveAddress: MutableLiveData<String> by lazy { MutableLiveData<String>() }
}