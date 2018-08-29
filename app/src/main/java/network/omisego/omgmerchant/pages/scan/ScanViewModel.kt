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
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.model.params.WalletParams
import co.omisego.omisego.model.transaction.Transaction
import co.omisego.omisego.model.transaction.TransactionSource
import co.omisego.omisego.model.transaction.send.TransactionCreateParams
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.model.APIResult

class ScanViewModel(
    private val app: Application,
    private val scanRepository: ScanRepository
) : AndroidViewModel(app) {
    lateinit var args: ScanFragmentArgs
    val liveTransaction: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }
    val liveWallet: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }
    val verifier: ScanAddressVerifier by lazy {
        ScanAddressVerifier(this).apply {
            this.getTransactionCreateParams = this@ScanViewModel::provideTransactionCreateParams
        }
    }

    val amountText: String
        get() = app.getString(R.string.scan_amount, args.amount.toBigDecimal(), args.token.symbol)

    val title: String
        get() {
            return if (args.transactionType == SCAN_RECEIVE) {
                app.getString(R.string.scan_title_payment)
            } else {
                app.getString(R.string.scan_title_topup)
            }
        }

    fun transfer(params: TransactionCreateParams) = scanRepository.transfer(params, liveTransaction)

    fun getUserWallet() {
        scanRepository.getUserWallet(WalletParams(verifier.recentAddress), liveWallet)
    }

    fun saveFeedback(transaction: Transaction) {
        scanRepository.saveFeedback(args.transactionType, transaction)
    }

    fun saveFeedback(wallet: Wallet) {
        scanRepository.saveFeedback(args.transactionType, TransactionSource(
            wallet.address,
            args.amount.toBigDecimal().multiply(args.token.subunitToUnit),
            args.token.id,
            args.token,
            wallet.userId,
            wallet.user,
            null,
            null
        ))
    }

    fun provideTransactionCreateParams(payload: String): TransactionCreateParams {
        when (args.transactionType) {
            SCAN_RECEIVE -> {
                return TransactionCreateParams(
                    fromAddress = payload,
                    toAddress = scanRepository.loadWallet().address,
                    amount = args.amount.toBigDecimal().multiply(args.token.subunitToUnit).setScale(0),
                    tokenId = args.token.id
                )
            }
            else -> {
                return TransactionCreateParams(
                    fromAddress = scanRepository.loadWallet().address,
                    toAddress = payload,
                    amount = args.amount.toBigDecimal().multiply(args.token.subunitToUnit).setScale(0),
                    tokenId = args.token.id
                )
            }
        }
    }
}
