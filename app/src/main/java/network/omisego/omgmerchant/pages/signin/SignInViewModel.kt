package network.omisego.omgmerchant.pages.signin

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import co.omisego.omisego.model.AuthenticationToken
import co.omisego.omisego.model.params.LoginParams
import kotlinx.coroutines.experimental.Deferred
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.LiveState
import network.omisego.omgmerchant.extensions.mutableLiveDataOf
import network.omisego.omgmerchant.extensions.runBelowP
import network.omisego.omgmerchant.extensions.runOnP
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
    private val app: Application,
    private val signInRepository: SignInRepository,
    private val biometricHandler: BiometricHandler
) : AndroidViewModel(app) {
    private val liveState: LiveState<SignInState> by lazy {
        LiveState(SignInState("", "", context.getString(R.string.sign_in_button), false))
    }
    private val liveByPassValidation: MutableLiveData<Boolean> by lazy { mutableLiveDataOf(true) }
    private val liveAPIResult: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }
    val liveBtnText: LiveData<String> by lazy { liveState.mapPropChanged { it.btnText } }
    val liveLoading: LiveData<Boolean> by lazy { liveState.mapPropChanged { it.loading } }
    val liveToast: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val emailValidator: Validator by lazy { EmailValidator(liveByPassValidation) }
    val passwordValidator: Validator by lazy { PasswordValidator(liveByPassValidation) }

    /* Biometric LiveData */
    val liveAuthenticationError: MutableLiveData<Pair<Int, CharSequence?>> by lazy { MutableLiveData<Pair<Int, CharSequence?>>() }
    val liveAuthenticationSucceeded: MutableLiveData<BiometricPrompt.CryptoObject> by lazy { MutableLiveData<BiometricPrompt.CryptoObject>() }
    val liveAuthenticationFailed: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    val liveAuthenticationHelp: MutableLiveData<Pair<Int, CharSequence?>> by lazy { MutableLiveData<Pair<Int, CharSequence?>>() }

    /* OnClick LiveData */
    val liveShowPre28FingerprintDialog: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    private lateinit var biometricCallback: BiometricCallback

    private var prompt: BiometricPrompt? = null

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
        return signInRepository.signIn(LoginParams(email, password), liveAPIResult)
    }

    fun saveCredential(data: AuthenticationToken): Deferred<Unit> {
        Storage.saveUser(data.user)
        return Storage.saveCredential(Credential(
            data.userId,
            data.authenticationToken
        ))
    }

    fun saveUserEmail(email: String) {
        Storage.saveUserEmail(email)
    }

    fun loadUserEmail(): String = Storage.loadUserEmail()

    fun loadUserPassword(): String = Storage.loadFingerprintCredential()

    fun handleFingerprintClick() {
        /*
       * The BiometricPrompt is now only supported android P or above.
       * BiometricPrompt compatible version will be coming with androidX package next release:
       * https://android.googlesource.com/platform/frameworks/support/+/androidx-master-dev/biometric/src/main/java/androidx/biometrics/BiometricPrompt.java
       */
        runOnP {
            biometricCallback = BiometricCallback(
                liveAuthenticationError,
                liveAuthenticationSucceeded,
                liveAuthenticationFailed,
                liveAuthenticationHelp
            )

            prompt = BiometricPrompt.Builder(app)
                .setTitle("Sign-in")
                .setSubtitle("Sign-in to your merchant account")
                .setDescription("In order to use the Fingerprint sensor we need your authorization first.")
                .setNegativeButton("Cancel", app.mainExecutor, DialogInterface.OnClickListener { dialog, _ -> dialog?.dismiss() })
                .build()

            prompt?.authenticate(
                biometricHandler.createCancellationSignal(),
                app.mainExecutor,
                biometricCallback
            )
        }

        runBelowP {
            liveShowPre28FingerprintDialog.value = true
        }
    }

    fun isFingerprintAvailable() = signInRepository.loadFingerprintOption()

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
