package network.omisego.omgmerchant.repository

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.custom.OMGCallback
import co.omisego.omisego.custom.retrofit2.adapter.OMGCall
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.Transaction
import co.omisego.omisego.model.TransactionConsumption
import co.omisego.omisego.model.TransactionRequest
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.model.pagination.PaginationList
import co.omisego.omisego.model.params.AccountWalletListParams
import co.omisego.omisego.model.params.LoginParams
import co.omisego.omisego.model.params.TokenListParams
import co.omisego.omisego.model.params.TransactionConsumptionActionParams
import co.omisego.omisego.model.params.admin.TransactionListParams
import co.omisego.omisego.model.params.TransactionRequestParams
import co.omisego.omisego.model.params.WalletParams
import co.omisego.omisego.model.params.admin.TransactionConsumptionParams
import co.omisego.omisego.model.params.admin.TransactionCreateParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import network.omisego.omgmerchant.extensions.subscribe
import network.omisego.omgmerchant.extensions.subscribeEvent
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.network.ClientProvider
import network.omisego.omgmerchant.network.ParamsCreator
import network.omisego.omgmerchant.storage.Storage

class RemoteRepository {
    private val paramsCreator by lazy { ParamsCreator() }
    fun loadWallet(params: WalletParams, liveWallet: MutableLiveData<Event<APIResult>>) {
        ClientProvider.client.getWallet(params).subscribeEvent(liveWallet)
    }

    fun loadWallet(params: WalletParams): OMGCall<Wallet> {
        return ClientProvider.client.getWallet(params)
    }

    fun loadWalletAndSave(params: AccountWalletListParams) {
        GlobalScope.async(Dispatchers.IO) {
            ClientProvider.client
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

    fun loadAccounts(liveAPIResult: MutableLiveData<Event<APIResult>>) {
        ClientProvider.client
            .getAccounts(paramsCreator.createLoadAccountParams())
            .subscribeEvent(liveAPIResult)
    }

    fun loadTransactionRequest(params: TransactionRequestParams, liveAPIResult: MutableLiveData<Event<APIResult>>) {
        ClientProvider.client.getTransactionRequest(params)
            .subscribeEvent(liveAPIResult)
    }

    fun loadTransactionRequest(params: TransactionRequestParams): OMGCall<TransactionRequest> {
        return ClientProvider.client.getTransactionRequest(params)
    }

    fun loadTransactions(params: TransactionListParams, liveAPIResult: MutableLiveData<Event<APIResult>>) {
        ClientProvider.client
            .getTransactions(params)
            .subscribeEvent(liveAPIResult)
    }

    fun listTokens(params: TokenListParams, liveAPIResult: MutableLiveData<APIResult>) {
        ClientProvider.client
            .getTokens(params)
            .subscribe(liveAPIResult)
    }

    fun signIn(params: LoginParams, liveAPIResult: MutableLiveData<Event<APIResult>>) {
        ClientProvider.client.login(params).subscribeEvent(liveAPIResult)
    }

    fun transfer(params: TransactionCreateParams, liveTransaction: MutableLiveData<Event<APIResult>>) {
        ClientProvider.client.createTransaction(params).subscribeEvent(liveTransaction)
    }

    fun transfer(params: TransactionCreateParams): OMGCall<Transaction> {
        return ClientProvider.client.createTransaction(params)
    }

    fun rejectTransactionConsumption(params: TransactionConsumptionActionParams, liveResult: MutableLiveData<Event<APIResult>>) {
        ClientProvider.client.rejectTransactionConsumption(params).subscribeEvent(liveResult)
    }

    fun consumeTransactionRequest(params: TransactionConsumptionParams, liveTransactionConsumption: MutableLiveData<Event<APIResult>>) {
        ClientProvider.client.consumeTransactionRequest(params).subscribeEvent(liveTransactionConsumption)
    }

    fun consumeTransactionRequest(params: TransactionConsumptionParams): OMGCall<TransactionConsumption> {
        return ClientProvider.client.consumeTransactionRequest(params)
    }
}
