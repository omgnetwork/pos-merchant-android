package network.omisego.omgmerchant.pages.scan

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 16/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import co.omisego.omisego.extension.bd
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.transaction.Transaction
import co.omisego.omisego.model.transaction.send.TransactionCreateParams
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.model.APIResult

class ScanViewModel(
    private val app: Application,
    private val scanRepository: ScanRepository
) : AndroidViewModel(app) {
    lateinit var transactionType: String
    lateinit var token: Token
    var amount: Double = 0.0
    val liveTransaction: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }
    val verifier: ScanAddressVerifier by lazy {
        ScanAddressVerifier(this).apply {
            this.getTransactionCreateParams = this@ScanViewModel::provideTransactionCreateParams
        }
    }

    val amountText: String
        get() = app.getString(R.string.scan_amount, amount, token.symbol)

    val title: String
        get() {
            return if (transactionType == "receive") {
                app.getString(R.string.scan_title_payment)
            } else {
                app.getString(R.string.scan_title_topup)
            }
        }

    fun transfer(params: TransactionCreateParams) = scanRepository.transfer(params, liveTransaction)

    fun saveFeedback(transactionType: String, transaction: Transaction) {
        scanRepository.saveFeedback(transactionType, transaction)
    }

    private fun provideTransactionCreateParams(payload: String): TransactionCreateParams {
        when (transactionType) {
            "receive" -> {
                return TransactionCreateParams(
                    fromAddress = payload,
                    toAddress = scanRepository.loadWallet().address,
                    amount = amount.bd.multiply(token.subunitToUnit).setScale(0),
                    tokenId = token.id
                )
            }
            else -> {
                return TransactionCreateParams(
                    fromAddress = scanRepository.loadWallet().address,
                    toAddress = payload,
                    amount = amount.bd.multiply(token.subunitToUnit).setScale(0),
                    tokenId = token.id
                )
            }
        }
    }
}
