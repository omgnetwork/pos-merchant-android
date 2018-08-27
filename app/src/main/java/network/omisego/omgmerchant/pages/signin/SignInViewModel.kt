package network.omisego.omgmerchant.pages.signin

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.AuthenticationToken
import co.omisego.omisego.model.params.LoginParams
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.LiveState
import network.omisego.omgmerchant.extensions.mutableLiveDataOf
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.Credential
import network.omisego.omgmerchant.storage.Storage
import network.omisego.omgmerchant.utils.Contextor.context
import network.omisego.omgmerchant.utils.EmailValidator
import network.omisego.omgmerchant.utils.PasswordValidator
import network.omisego.omgmerchant.utils.Validator
import network.omisego.omgmerchant.utils.mapPropChanged

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class SignInViewModel(
    private val signInRepository: SignInRepository
) : ViewModel() {
    private val liveState: LiveState<SignInState> by lazy {
        LiveState(SignInState("", "", context.getString(R.string.sign_in_button), false))
    }
    private val liveByPassValidation: MutableLiveData<Boolean> by lazy { mutableLiveDataOf(true) }
    val liveBtnText: LiveData<String> by lazy { liveState.mapPropChanged { it.btnText } }
    val liveLoading: LiveData<Boolean> by lazy { liveState.mapPropChanged { it.loading } }
    val emailValidator: Validator by lazy { EmailValidator(liveByPassValidation) }
    val passwordValidator: Validator by lazy { PasswordValidator(liveByPassValidation) }

    private var isSignIn: Boolean = false

    fun updateEmail(text: CharSequence) {
        liveState.state { it.copy(email = text.toString()) }
    }

    fun updatePassword(text: CharSequence) {
        liveState.state { it.copy(password = text.toString()) }
    }

    fun signin(): LiveData<APIResult>? {
        val (email, password) = liveState.value ?: return null
        liveByPassValidation.value = false
        arrayOf(emailValidator, passwordValidator).find { !it.lastResult.pass }?.let { return null }
        isSignIn = true
        return signInRepository.signIn(LoginParams(email, password))
    }

    fun saveCredential(data: AuthenticationToken) {
        Storage.saveCredential(Credential(
            data.userId,
            data.authenticationToken
        ))
    }

    fun showLoading(text: String) {
        liveState.state { it.copy(loading = true, btnText = text) }
    }

    fun hideLoading(text: String) {
        liveState.state { it.copy(loading = false, btnText = text) }
    }

    override fun onCleared() {
        super.onCleared()
        emailValidator.onCleared()
        passwordValidator.onCleared()
    }

    init {
        liveState.state { it.copy(loading = false) }
    }
}
