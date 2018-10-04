package network.omisego.omgmerchant.pages.confirm

import android.arch.lifecycle.MutableLiveData
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.params.WalletParams
import co.omisego.omisego.model.transaction.Transaction
import co.omisego.omisego.model.transaction.TransactionSource
import co.omisego.omisego.model.transaction.send.TransactionCreateParams
import network.omisego.omgmerchant.extensions.subscribe
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.network.ClientProvider
import network.omisego.omgmerchant.pages.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.storage.Storage
import java.util.Date

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 16/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class ConfirmRepository {
    fun loadWallet() = Storage.loadWallet()!!
    fun transfer(params: TransactionCreateParams, liveTransaction: MutableLiveData<APIResult>) =
        ClientProvider.client.transfer(params).subscribe(liveTransaction)

    fun getUserWallet(params: WalletParams, liveWallet: MutableLiveData<APIResult>) {
        ClientProvider.client.getWallet(params).subscribe(liveWallet)
    }

    fun saveFeedback(transactionType: String, transaction: Transaction) {
        val feedback =
            if (transactionType.equals(SCAN_RECEIVE, true)) Feedback(
                true,
                transactionType,
                transaction.createdAt,
                transaction.from
            ) else {
                Feedback(
                    true,
                    transactionType,
                    transaction.createdAt,
                    transaction.to
                )
            }
        Storage.saveFeedback(feedback)
    }

    fun saveFeedback(transactionType: String, source: TransactionSource, error: APIError? = null) {
        val feedback =
            if (transactionType.equals(SCAN_RECEIVE, true)) Feedback(
                false,
                transactionType,
                Date(),
                source,
                error
            ) else {
                Feedback(
                    false,
                    transactionType,
                    Date(),
                    source,
                    error
                )
            }
        Storage.saveFeedback(feedback)
    }
}
