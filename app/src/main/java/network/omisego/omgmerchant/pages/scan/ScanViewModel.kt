package network.omisego.omgmerchant.pages.scan

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 16/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.extension.bd
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.transaction.Transaction
import co.omisego.omisego.model.transaction.send.TransactionCreateParams
import network.omisego.omgmerchant.model.APIResult

class ScanViewModel(
    private val scanRepository: ScanRepository
) : ViewModel() {
    lateinit var token: Token
    var amount: Double = 0.0
    val liveTransaction: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }
    val verifier: AddressVerifier by lazy {
        AddressVerifier(this).apply {
            this.getTransactionCreateParams = this@ScanViewModel::provideTransactionCreateParams
        }
    }

    fun transfer(params: TransactionCreateParams) = scanRepository.transfer(params, liveTransaction)

    fun saveFeedback(transactionType: String, transaction: Transaction) {
        scanRepository.saveFeedback(transactionType, transaction)
    }

    private fun provideTransactionCreateParams(payload: String): TransactionCreateParams {
        return TransactionCreateParams(
            fromAddress = payload,
            toAddress = scanRepository.loadWallet().address,
            amount = amount.bd.multiply(token.subunitToUnit).setScale(0),
            tokenId = token.id
        )
    }
}
