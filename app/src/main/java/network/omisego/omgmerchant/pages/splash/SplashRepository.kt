package network.omisego.omgmerchant.pages.splash

import co.omisego.omisego.model.Account
import network.omisego.omgmerchant.storage.Storage

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class SplashRepository {
    fun loadAccount(): Account? {
        return Storage.loadAccount()
    }
}
