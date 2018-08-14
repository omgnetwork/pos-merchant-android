package network.omisego.omgmerchant.utils

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.util.Patterns
import network.omisego.omgmerchant.model.ValidateResult

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

sealed class Validator(
    open var byPass: LiveData<Boolean>
) : LifecycleOwner {
    abstract val lifecycleOwnerRegistry: LifecycleRegistry
    abstract var latestText: String
    abstract var lastResult: ValidateResult
    abstract var updateUI: ((ValidateResult) -> Unit)?
    abstract fun onCleared()
    abstract fun check(text: String, updateUI: ((ValidateResult) -> Unit)?): ValidateResult

    class EmailValidator(override var byPass: LiveData<Boolean>) : Validator(byPass) {
        override var updateUI: ((ValidateResult) -> Unit)? = null
        override val lifecycleOwnerRegistry: LifecycleRegistry by lazy { LifecycleRegistry(this) }
        override fun getLifecycle(): Lifecycle = lifecycleOwnerRegistry
        override var lastResult: ValidateResult = ValidateResult(true)
        override var latestText: String = ""
        private val isInvalidEmailFormat: (String) -> Boolean = {
            !Patterns.EMAIL_ADDRESS.matcher(it).matches()
        }

        override fun check(text: String, updateUI: ((ValidateResult) -> Unit)?): ValidateResult {
            this.updateUI = updateUI
            this.latestText = text
            lastResult = when {
                byPass.value == false && isInvalidEmailFormat(text) -> ValidateResult(false, "Email Address is invalid format")
                else -> ValidateResult(true)
            }
            updateUI?.invoke(lastResult)
            return lastResult
        }

        override fun onCleared() {
            lifecycleOwnerRegistry.markState(Lifecycle.State.DESTROYED)
        }

        init {
            lifecycleOwnerRegistry.markState(Lifecycle.State.STARTED)
            byPass.observe(this, Observer { check(this.latestText, this.updateUI) })
        }
    }

    class PasswordValidator(override var byPass: LiveData<Boolean>) : Validator(byPass) {
        override var updateUI: ((ValidateResult) -> Unit)? = null
        override val lifecycleOwnerRegistry: LifecycleRegistry by lazy { LifecycleRegistry(this) }
        override var latestText: String = ""
        override var lastResult: ValidateResult = ValidateResult(true)
        private val isPasswordLessThanEight: (String) -> Boolean = {
            it.length < 8
        }

        override fun getLifecycle(): Lifecycle = lifecycleOwnerRegistry
        override fun check(text: String, updateUI: ((ValidateResult) -> Unit)?): ValidateResult {
            this.updateUI = updateUI
            this.latestText = text
            lastResult = when {
                byPass.value == false && isPasswordLessThanEight(text) -> ValidateResult(false, "Password must contain at least 8 characters")
                else -> ValidateResult(true)
            }
            updateUI?.invoke(lastResult)
            return lastResult
        }

        override fun onCleared() {
            lifecycleOwnerRegistry.markState(Lifecycle.State.DESTROYED)
        }

        init {
            lifecycleOwnerRegistry.markState(Lifecycle.State.STARTED)
            byPass.observe(this, Observer { check(this.latestText, this.updateUI) })
        }
    }
}
