package network.omisego.omgmerchant.pages.signin

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import android.view.View
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.AuthenticationToken
import co.omisego.omisego.model.params.LoginParams

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class SignInViewModel(
    private val signInRepository: SignInRepository
) : ViewModel() {
    val liveEmail: MutableLiveData<String> = MutableLiveData()
    private val livePassword: MutableLiveData<String> = MutableLiveData()
    private val errorResponse: MutableLiveData<APIError> by lazy { MutableLiveData<APIError>() }
    private val successResponse: MutableLiveData<AuthenticationToken> by lazy { MutableLiveData<AuthenticationToken>() }
    private var isSignIn: Boolean = false

    fun updateEmail(text: CharSequence) {
        liveEmail.value = text.toString()
    }

    fun updatePassword(text: CharSequence) {
        livePassword.value = text.toString()
    }

    fun signin(view: View) {
        isSignIn = true
        val email = liveEmail.value ?: return
        val password = livePassword.value ?: return
        Log.d("SignIn", "email: $email, password: $password")
        signInRepository.signIn(LoginParams(email, password), successResponse to errorResponse)
    }

    fun subscribeSignInResult() = successResponse to errorResponse
}
