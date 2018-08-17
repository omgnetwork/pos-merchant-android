package network.omisego.omgmerchant.pages.main

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 17/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.model.Account
import network.omisego.omgmerchant.storage.Storage

class AccountRepository {
    fun getAccount(): Account? = Storage.loadAccount()
}
