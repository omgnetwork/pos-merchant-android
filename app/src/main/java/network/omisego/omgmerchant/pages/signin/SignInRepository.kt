package network.omisego.omgmerchant.pages.signin

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import co.omisego.omisego.model.User
import co.omisego.omisego.model.params.LoginParams
import network.omisego.omgmerchant.extensions.subscribe
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.Credential
import network.omisego.omgmerchant.network.ClientProvider
import network.omisego.omgmerchant.storage.Storage

class SignInRepository {
    fun signIn(params: LoginParams, liveAPIResult: MutableLiveData<APIResult>): LiveData<APIResult> {
        return ClientProvider.client.login(params).subscribe(liveAPIResult)
    }

    fun loadFingerprintOption() = Storage.loadFingerprintOption()

    fun loadUserEmail() = Storage.loadUserEmail()

    fun loadFingerprintCredential() = Storage.loadFingerprintCredential()

    fun saveUser(user: User) = Storage.saveUser(user)

    fun saveCredential(credential: Credential) = Storage.saveCredential(credential)

    fun saveUserEmail(email: String) = Storage.saveUserEmail(email)
}
