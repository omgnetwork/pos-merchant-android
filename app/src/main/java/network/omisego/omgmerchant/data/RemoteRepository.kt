package network.omisego.omgmerchant.data

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import co.omisego.omisego.custom.OMGCallback
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.model.pagination.PaginationList
import co.omisego.omisego.model.params.AccountListParams
import co.omisego.omisego.model.params.AccountWalletListParams
import co.omisego.omisego.model.params.LoginParams
import co.omisego.omisego.model.params.TokenListParams
import co.omisego.omisego.model.params.WalletParams
import co.omisego.omisego.model.transaction.list.TransactionListParams
import co.omisego.omisego.model.transaction.send.TransactionCreateParams
import kotlinx.coroutines.experimental.async
import network.omisego.omgmerchant.extensions.subscribe
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.network.ClientProvider
import network.omisego.omgmerchant.storage.Storage

class RemoteRepository() {
    fun loadWallet(params: WalletParams, liveWallet: MutableLiveData<APIResult>) {
        ClientProvider.client.getWallet(params).subscribe(liveWallet)
    }

    fun loadWalletAndSave(params: AccountWalletListParams) {
        async {
            ClientProvider.deferredClient.await()
                .getAccountWallets(params)
                .enqueue(object : OMGCallback<PaginationList<Wallet>> {
                    override fun fail(response: OMGResponse<APIError>) {
                    }

                    override fun success(response: OMGResponse<PaginationList<Wallet>>) {
                        Storage.saveWallet(response.data.data.findLast { it.name == "primary" }!!)
                    }
                })
        }
    }

    fun loadAccounts(): LiveData<APIResult> {
        return ClientProvider.client
            .getAccounts(AccountListParams.create(searchTerm = null))
            .subscribe()
    }

    fun loadTransactions(params: TransactionListParams): LiveData<APIResult> {
        return ClientProvider.client
            .getTransactions(params)
            .subscribe()
    }

    fun listTokens(params: TokenListParams, liveAPIResult: MutableLiveData<APIResult>) {
        async {
            ClientProvider.deferredClient.await()
                .getTokens(params)
                .subscribe(liveAPIResult)
        }
    }

    fun signIn(params: LoginParams, liveAPIResult: MutableLiveData<APIResult>): LiveData<APIResult> {
        return ClientProvider.client.login(params).subscribe(liveAPIResult)
    }

    fun transfer(params: TransactionCreateParams, liveTransaction: MutableLiveData<APIResult>) {
        ClientProvider.client.transfer(params).subscribe(liveTransaction)
    }
}
