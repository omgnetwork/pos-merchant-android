package network.omisego.omgmerchant.databinding

import android.databinding.BindingAdapter
import android.support.design.widget.TextInputLayout
import network.omisego.omgmerchant.model.ValidateResult
import network.omisego.omgmerchant.utils.MinimalTextChangeListener
import network.omisego.omgmerchant.utils.Validator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object TextInputLayoutBinding {
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