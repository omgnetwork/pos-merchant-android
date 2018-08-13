package network.omisego.omgmerchant.databinding

import android.databinding.BindingAdapter
import android.support.design.widget.TextInputLayout
import network.omisego.omgmerchant.utils.MinimalTextChangeListener
import network.omisego.omgmerchant.utils.Validator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object TextInputLayoutUtil {
    @JvmStatic
    @BindingAdapter("validator")
    fun validate(view: TextInputLayout, validator: Validator) {
        view.editText?.addTextChangedListener(MinimalTextChangeListener {
            val (pass, reason) = validator.check(it.toString())
            if (!pass) {
                view.error = reason
                view.isErrorEnabled = true
            } else {
                view.isErrorEnabled = false
            }
        })
    }
}