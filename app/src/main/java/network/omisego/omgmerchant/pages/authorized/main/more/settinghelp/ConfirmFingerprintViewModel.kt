package network.omisego.omgmerchant.pages.authorized.main.more.settinghelp

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.AuthenticationToken
import co.omisego.omisego.model.params.LoginParams
import kotlinx.coroutines.experimental.Deferred
import network.omisego.omgmerchant.data.LocalRepository
import network.omisego.omgmerchant.data.RemoteRepository
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.Credential

class ConfirmFingerprintViewModel(
    val localRepository: LocalRepository,
    val remoteRepository: RemoteRepository
) : ViewModel() {
    val liveAPIResult: MutableLiveData<Event<APIResult>> by lazy { MutableLiveData<Event<APIResult>>() }

    fun signIn(password: String) {
        remoteRepository.signIn(LoginParams(
            localRepository.loadUserEmail(),
            password
        ), liveAPIResult)
    }

    fun saveCredential(data: AuthenticationToken): Deferred<Unit> {
        localRepository.saveUser(data.user)
        return localRepository.saveCredential(
            Credential(
                data.userId,
                data.authenticationToken
            )
        )
    }

    fun saveUserPassword(password: String) {
        localRepository.savePassword(password)
    }
}
