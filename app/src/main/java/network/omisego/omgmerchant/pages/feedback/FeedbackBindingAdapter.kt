package network.omisego.omgmerchant.pages.feedback

import android.arch.lifecycle.LiveData
import android.databinding.BindingAdapter
import android.view.View
import network.omisego.omgmerchant.model.Feedback

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 28/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object FeedbackBindingAdapter {
    @JvmStatic
    @BindingAdapter("show")
    fun showing(view: View, liveLoading: LiveData<Boolean>) {
        liveLoading.observeForever {
            if (it == true) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }
    }

    @JvmStatic
    @BindingAdapter("show", "feedback")
    fun showingWithFeedback(view: View, liveLoading: LiveData<Boolean>, feedback: LiveData<Feedback>) {
        liveLoading.observeForever {
            if (!feedback.value!!.success && it == false) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }
    }
}