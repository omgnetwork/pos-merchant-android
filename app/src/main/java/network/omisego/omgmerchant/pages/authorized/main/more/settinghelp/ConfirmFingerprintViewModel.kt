package network.omisego.omgmerchant.pages.authorized.main.more.settinghelp

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.omisego.omisego.model.AdminAuthenticationToken
import kotlinx.coroutines.Deferred
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.Credential
import network.omisego.omgmerchant.network.ParamsCreator
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository

class ConfirmFingerprintViewModel(
    val localRepository: LocalRepository,
    val remoteRepository: RemoteRepository,
    private val paramsCreator: ParamsCreator = ParamsCreator()
) : ViewModel() {
    val liveAPIResult: MutableLiveData<Event<APIResult>> by lazy { MutableLiveData<Event<APIResult>>() }

    fun signIn(password: String) {
        remoteRepository.signIn(paramsCreator.createLoginParams(
            localRepository.loadUserEmail(),
            password
        ), liveAPIResult)
    }

    fun saveCredential(data: AdminAuthenticationToken): Deferred<Unit> {
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
