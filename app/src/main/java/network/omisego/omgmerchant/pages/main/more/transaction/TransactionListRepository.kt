package network.omisego.omgmerchant.pages.main.more.transaction

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.LiveData
import co.omisego.omisego.model.transaction.list.TransactionListParams
import network.omisego.omgmerchant.extensions.subscribe
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.network.ClientProvider
import network.omisego.omgmerchant.storage.Storage

class TransactionListRepository {
    fun getTransactions(params: TransactionListParams): LiveData<APIResult> {
        return ClientProvider.client
            .getTransactions(params)
            .subscribe()
    }

    fun getAccount() = Storage.loadAccount()
}