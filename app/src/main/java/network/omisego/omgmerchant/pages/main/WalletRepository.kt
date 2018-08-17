package network.omisego.omgmerchant.pages.main

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 16/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import co.omisego.omisego.custom.OMGCallback
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.model.pagination.PaginationList
import co.omisego.omisego.model.params.AccountWalletListParams
import network.omisego.omgmerchant.extensions.subscribe
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.network.ClientProvider
import network.omisego.omgmerchant.storage.Storage

class WalletRepository {
    fun getWallet(params: AccountWalletListParams, liveWallet: MutableLiveData<APIResult>): LiveData<APIResult> {
        return ClientProvider.client
            .getAccountWallets(params)
            .subscribe(liveWallet)
    }

    fun loadWalletAndSave(params: AccountWalletListParams) {
        return ClientProvider.client
            .getAccountWallets(params)
            .enqueue(object : OMGCallback<PaginationList<Wallet>> {
                override fun fail(response: OMGResponse<APIError>) {
                    Log.i(this.javaClass.simpleName, response.data.description)
                }

                override fun success(response: OMGResponse<PaginationList<Wallet>>) {
                    Storage.saveWallet(response.data.data.findLast { it.name == "primary" }!!)
                }
            })
    }
}