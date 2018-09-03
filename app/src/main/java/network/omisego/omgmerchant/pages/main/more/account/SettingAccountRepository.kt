package network.omisego.omgmerchant.pages.main.more.account

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.LiveData
import co.omisego.omisego.model.Account
import co.omisego.omisego.model.params.AccountListParams
import network.omisego.omgmerchant.extensions.subscribe
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.network.ClientProvider
import network.omisego.omgmerchant.storage.Storage

class SettingAccountRepository {
    fun loadAccounts(): LiveData<APIResult> {
        return ClientProvider.client
            .getAccounts(AccountListParams.create(searchTerm = null))
            .subscribe()
    }

    fun saveAccount(account: Account) = Storage.saveAccount(account)

    fun getCurrentAccount() = Storage.loadAccount()
}
