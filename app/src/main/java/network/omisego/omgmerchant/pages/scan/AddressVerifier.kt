package network.omisego.omgmerchant.pages.scan

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 16/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.transaction.Transaction
import co.omisego.omisego.model.transaction.send.TransactionCreateParams
import co.omisego.omisego.qrcode.scanner.OMGQRScannerContract

class AddressVerifier(
    val viewModel: ScanViewModel
) : OMGQRScannerContract.Logic.Verifier, LifecycleOwner {
    override var postVerification: OMGQRScannerContract.Logic.PostVerification? = null
    private val lifecycleRegistry: LifecycleRegistry by lazy { LifecycleRegistry(this) }
    var getTransactionCreateParams: ((payload: String) -> TransactionCreateParams)? = null

    override fun getLifecycle() = lifecycleRegistry
    override fun onCanceled() {
        postVerification?.onStopLoading()
    }

    override fun onDecoded(payload: String) {
        val handleSuccess: (Transaction) -> Unit = {
            handleVerification(payload)
        }
        val handleFail: (APIError) -> Unit = {
            postVerification?.onStopLoading()
        }
        getTransactionCreateParams?.let { it ->
            viewModel.transfer(
                it.invoke(payload)
            ).observe(this, Observer {
                it?.handle(
                    handleSuccess = handleSuccess,
                    handleError = handleFail
                )
            })
        }
    }

    private fun handleVerification(payload: String) {
        postVerification?.onStopLoading()
        postVerification?.onRemoveCache(payload)
    }

    fun register() {
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
    }

    fun unregister() {
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
    }
}