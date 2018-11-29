package network.omisego.omgmerchant.databinding

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 8/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.LiveData
import androidx.databinding.BindingAdapter
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.helper.HelperContext

object BindingTextView {
    private val fadeInAnimation by lazy {
        AnimationUtils.loadAnimation(HelperContext.context, R.anim.fade_in)
    }

    @JvmStatic
    @BindingAdapter("animateAlpha")
    fun animateAlpha(view: TextView, liveVisibility: LiveData<Boolean>) {
        liveVisibility.observeForever { shouldShow ->
            if (shouldShow == true) {
                view.visibility = View.VISIBLE
                view.startAnimation(fadeInAnimation)
            } else {
                view.visibility = View.GONE
            }
        }
    }
}
