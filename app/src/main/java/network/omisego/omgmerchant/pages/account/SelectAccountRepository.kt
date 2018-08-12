package network.omisego.omgmerchant.pages.account

import android.arch.lifecycle.MutableLiveData
import co.omisego.omisego.custom.OMGCallback
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Account
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.pagination.PaginationList
import co.omisego.omisego.model.params.AccountListParams
import network.omisego.omgmerchant.network.ClientProvider

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class SelectAccountRepository {
    fun loadAccounts(liveResponse: Pair<MutableLiveData<List<Account>>, MutableLiveData<APIError>>) {
        ClientProvider.client
            ?.getAccounts(AccountListParams.create(searchTerm = null))
            ?.enqueue(object : OMGCallback<PaginationList<Account>> {
                override fun fail(response: OMGResponse<APIError>) {
                    liveResponse.second.value = response.data
                }

                override fun success(response: OMGResponse<PaginationList<Account>>) {
                    liveResponse.first.value = response.data.data
                }
            })
    }
}
