package network.omisego.omgmerchant.pages.unauthorized.signin

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

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
import network.omisego.omgmerchant.data.LocalRepository
import network.omisego.omgmerchant.data.RemoteRepository
import network.omisego.omgmerchant.extensions.mutableLiveDataOf
import network.omisego.omgmerchant.extensions.runBelowM
import network.omisego.omgmerchant.extensions.runOnMToP
import network.omisego.omgmerchant.extensions.runOnP
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.Credential
import network.omisego.omgmerchant.utils.BiometricHelper
import network.omisego.omgmerchant.utils.Contextor.context
import network.omisego.omgmerchant.utils.EmailValidator
import network.omisego.omgmerchant.utils.PasswordValidator
import network.omisego.omgmerchant.utils.Validator
import network.omisego.omgmerchant.utils.mapPropChanged

class SignInViewModel(
    private val app: Application,
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val biometricHelper: BiometricHelper
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
                .setTitle(app.getString(R.string.dialog_fingerprint_title))
                .setSubtitle(app.getString(R.string.dialog_fingerprint_subtitle))
                .setDescription(app.getString(R.string.dialog_fingerprint_description))
                .setNegativeButton(
                    app.getString(R.string.dialog_fingerprint_btn_cancel),
                    app.mainExecutor,
                    DialogInterface.OnClickListener { dialog, _ -> dialog?.dismiss() }
                )
                .build()

            prompt?.authenticate(
                biometricHelper.createCancellationSignal(),
                app.mainExecutor,
                biometricCallback
            )
        }

        runOnMToP {
            liveShowPre28FingerprintDialog.value = true
        }

        runBelowM {
            liveToast.value = app.getString(R.string.dialog_fingerprint_unsupported)
        }
    }

    fun isFingerprintAvailable() = localRepository.loadFingerprintOption()

    fun loadUserEmail(): String = localRepository.loadUserEmail()

    fun loadUserPassword(): String = localRepository.loadFingerprintCredential()

    fun signIn(): LiveData<APIResult>? {
        val (email, password) = liveState.value ?: return null
        liveByPassValidation.value = false
        arrayOf(emailValidator, passwordValidator).find { !it.validation.pass }?.let { return null }
        isSignIn = true
        return remoteRepository.signIn(LoginParams(email, password), liveAPIResult)
    }

    fun hasCredential(): Boolean = localRepository.hasCredential()

    fun saveCredential(data: AuthenticationToken): Deferred<Unit> {
        localRepository.saveUser(data.user)
        return localRepository.saveCredential(
            Credential(
                data.userId,
                data.authenticationToken
            )
        )
    }

    fun saveUserEmail(email: String) {
        localRepository.saveUserEmail(email)
    }

    fun showLoading(text: String) {
        liveState.state { it.copy(loading = true, btnText = text) }
    }

    fun hideLoading(text: String) {
        liveState.state { it.copy(loading = false, btnText = text) }
    }

    fun updateEmail(text: CharSequence) {
        liveState.state { it.copy(email = text.toString()) }
    }

    fun updatePassword(text: CharSequence) {
        liveState.state { it.copy(password = text.toString()) }
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
