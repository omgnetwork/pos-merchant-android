package network.omisego.omgmerchant.pages.signin

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import co.omisego.omisego.custom.OMGCallback
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.AuthenticationToken
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.params.LoginParams
import network.omisego.omgmerchant.model.Credential
import network.omisego.omgmerchant.network.ClientProvider
import network.omisego.omgmerchant.storage.Storage

class SignInRepository {
    fun signIn(params: LoginParams, liveResponse: Pair<MutableLiveData<AuthenticationToken>, MutableLiveData<APIError>>) {
        ClientProvider.client?.login(params)?.enqueue(object : OMGCallback<AuthenticationToken> {
            override fun fail(response: OMGResponse<APIError>) {
                liveResponse.second.value = response.data
            }

            override fun success(response: OMGResponse<AuthenticationToken>) {
                liveResponse.first.value = response.data
                Storage.saveCredential(Credential(
                    response.data.userId,
                    response.data.authenticationToken
                ))
            }
        })
    }
}
