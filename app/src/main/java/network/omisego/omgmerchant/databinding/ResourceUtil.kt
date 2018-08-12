package network.omisego.omgmerchant.databinding

import android.databinding.BindingAdapter
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.View

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object ResourceUtil {
    @JvmStatic
    @BindingAdapter("backgroundDrawable")
    fun setBackgroundDrawable(
        view: View,
        @DrawableRes drawableResId: Int
    ) {
        view.background = ContextCompat.getDrawable(view.context, drawableResId)
    }
}