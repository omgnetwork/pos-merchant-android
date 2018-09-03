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
import android.widget.Toast
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.transaction.Transaction
import co.omisego.omisego.model.transaction.send.TransactionCreateParams
import co.omisego.omisego.qrcode.scanner.OMGQRScannerContract
import com.crashlytics.android.Crashlytics
import network.omisego.omgmerchant.utils.Contextor

class ScanAddressVerifier(
    val viewModel: ScanViewModel
) : OMGQRScannerContract.Preview.Verifier, LifecycleOwner {
    override var postVerification: OMGQRScannerContract.Preview.PostVerification? = null
    private val lifecycleRegistry: LifecycleRegistry by lazy { LifecycleRegistry(this) }
    var getTransactionCreateParams: ((payload: String) -> TransactionCreateParams)? = null
    lateinit var recentAddress: String

    override fun getLifecycle() = lifecycleRegistry
    override fun onCanceled() {
        postVerification?.onStopLoading()
    }

    override fun onDecoded(payload: String) {
        recentAddress = payload
        val handleSuccess: (Transaction) -> Unit = { _ ->
            handleVerification()
        }
        val handleFail: (APIError) -> Unit = {
            postVerification?.onStopLoading()
        }

        try {
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
        } catch (e: Exception) {
            Toast.makeText(Contextor.context, e.message, Toast.LENGTH_SHORT).show()
            Crashlytics.log(e.message)
            e.printStackTrace()
        }
    }

    private fun handleVerification() {
        postVerification?.onStopLoading()
    }

    fun register() {
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
    }

    fun unregister() {
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
    }
}