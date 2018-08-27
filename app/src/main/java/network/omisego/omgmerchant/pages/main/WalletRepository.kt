package network.omisego.omgmerchant.pages.main

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 16/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.custom.OMGCallback
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.model.pagination.PaginationList
import co.omisego.omisego.model.params.AccountWalletListParams
import kotlinx.coroutines.experimental.async
import network.omisego.omgmerchant.network.ClientProvider
import network.omisego.omgmerchant.storage.Storage

class WalletRepository {
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
}