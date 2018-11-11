package network.omisego.omgmerchant.pages.authorized.scan

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 16/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import co.omisego.omisego.qrcode.scanner.OMGQRScannerContract
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.helper.HelperAmountFormatter
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.AmountFormat
import network.omisego.omgmerchant.pages.authorized.scan.handler.HandlerGetTransactionRequest
import network.omisego.omgmerchant.pages.authorized.scan.handler.HandlerGetUserWallet
import network.omisego.omgmerchant.repository.RemoteRepository

class ScanViewModel(
    private val app: Application,
    private val amountFormatter: HelperAmountFormatter,
    private val remoteRepository: RemoteRepository
) : AndroidViewModel(app), OMGQRScannerContract.Preview.Verifier {
    lateinit var args: ScanFragmentArgs
    override var postVerification: OMGQRScannerContract.Preview.PostVerification? = null

    /* MainFragment scope */
    lateinit var liveDirection: MutableLiveData<Event<NavDirections>>

    companion object {
        const val PREFIX_TX_REQUEST = "txr_"
    }

    val amountText: String
        get() {
            return amountFormatter.format(
                AmountFormat.Unit(args.amount.toBigDecimal(), args.token.subunitToUnit),
                args.token
            )
        }

    val title: String
        get() {
            return if (args.transactionType == SCAN_RECEIVE) {
                app.getString(R.string.scan_title_payment)
            } else {
                app.getString(R.string.scan_title_topup)
            }
        }

    override fun onCanceled() {}

    override fun onDecoded(payload: String) {
        retrieveUserInformation(payload)
    }

    fun removeCache(text: String?) {
        text ?: return
        postVerification?.onRemoveCache(cacheText = text)
    }

    fun retrieveUserInformation(rawPayload: String) {
        val payload: String
        val handler = if (rawPayload.startsWith(PREFIX_TX_REQUEST)) {
            val transactionRequestIds = rawPayload.split("|")
            payload = if (args.transactionType == SCAN_TOPUP) {
                transactionRequestIds[0]
            } else {
                transactionRequestIds[1]
            }

            HandlerGetTransactionRequest(remoteRepository).apply {
                args = this@ScanViewModel.args
                liveDirection = this@ScanViewModel.liveDirection
            }
        } else {
            payload = rawPayload
            HandlerGetUserWallet(remoteRepository).apply {
                args = this@ScanViewModel.args
                liveDirection = this@ScanViewModel.liveDirection
            }
        }
        handler.retrieve(payload)
    }
}
