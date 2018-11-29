package network.omisego.omgmerchant.databinding

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import network.omisego.omgmerchant.custom.MinimalTextChangeListener
import network.omisego.omgmerchant.custom.Validator
import network.omisego.omgmerchant.model.ValidateResult

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object BindingTextInputLayout {
    @JvmStatic
    @BindingAdapter("validator")
    fun validate(view: TextInputLayout, validator: Validator) {
        val updateUI: (ValidateResult) -> Unit = {
            val (pass, reason) = it
            if (!pass) {
                view.error = reason
                view.isErrorEnabled = true
            } else {
                view.isErrorEnabled = false
            }
        }
        view.editText?.addTextChangedListener(MinimalTextChangeListener {
            validator.check(it.toString(), updateUI)
        })
    }
}