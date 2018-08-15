package network.omisego.omgmerchant.pages.main.receive

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 8/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import android.support.v7.widget.AppCompatTextView
import android.view.View

object ReceiveCalculatorBindingAdapter {
    @SuppressLint("RestrictedApi")
    @JvmStatic
    @BindingAdapter("textCursorEnd")
    fun moveCursor(view: View, text: MutableLiveData<String>) {
        if (view is AppCompatTextView) {
            view.text = text.value
        }
    }
}
