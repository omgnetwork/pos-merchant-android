package network.omisego.omgmerchant.pages.signin

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.LiveData
import co.omisego.omisego.model.params.LoginParams
import network.omisego.omgmerchant.extensions.subscribe
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.network.ClientProvider

class SignInRepository {
    fun signIn(params: LoginParams): LiveData<APIResult> {
        return ClientProvider.client.login(params).subscribe()
    }
}
