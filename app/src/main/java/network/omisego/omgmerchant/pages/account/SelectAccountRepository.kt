package network.omisego.omgmerchant.pages.account

import android.arch.lifecycle.LiveData
import co.omisego.omisego.model.params.AccountListParams
import network.omisego.omgmerchant.extensions.subscribe
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.network.ClientProvider

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class SelectAccountRepository {
    fun loadAccounts(): LiveData<APIResult> {
        return ClientProvider.client
            .getAccounts(AccountListParams.create(searchTerm = null))
            .subscribe()
    }
}
