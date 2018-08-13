package network.omisego.omgmerchant.utils

import android.util.Patterns
import network.omisego.omgmerchant.model.ValidateResult

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

sealed class Validator(
    open var byPass: Boolean
) {
    abstract fun check(text: String): ValidateResult

    class EmailValidator(override var byPass: Boolean = true) : Validator(byPass) {
        private val isInvalidEmailFormat: (String) -> Boolean = {
            !Patterns.EMAIL_ADDRESS.matcher(it).matches()
        }

        override fun check(text: String): ValidateResult {
            if (byPass) return ValidateResult(true)
            return when {
                isInvalidEmailFormat(text) -> ValidateResult(false, "Email Address is invalid format")
                else -> ValidateResult(true)
            }
        }

        init {

        }
    }

    class PasswordValidator(override var byPass: Boolean = true) : Validator(byPass) {
        private val isPasswordLessThanEight: (String) -> Boolean = {
            it.length < 8
        }

        override fun check(text: String): ValidateResult {
            if (byPass) return ValidateResult(true)
            return when {
                isPasswordLessThanEight(text) -> ValidateResult(false, "Password must contain at least 8 characters")
                else -> ValidateResult(true)
            }
        }
    }
}
