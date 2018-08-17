package network.omisego.omgmerchant.pages.scan

import android.arch.lifecycle.MutableLiveData
import co.omisego.omisego.model.transaction.Transaction
import co.omisego.omisego.model.transaction.send.TransactionCreateParams
import network.omisego.omgmerchant.extensions.subscribe
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.network.ClientProvider
import network.omisego.omgmerchant.storage.Storage

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 16/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class ScanRepository {
    fun loadWallet() = Storage.loadWallet()!!
    fun transfer(params: TransactionCreateParams, liveTransaction: MutableLiveData<APIResult>) =
        ClientProvider.client.transfer(params).subscribe(liveTransaction)

    fun saveFeedback(transactionType: String, transaction: Transaction) {
        Storage.saveFeedback(
            Feedback(
                transactionType,
                transaction.createdAt,
                transaction.from
            )
        )
    }
}
