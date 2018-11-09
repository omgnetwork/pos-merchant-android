package network.omisego.omgmerchant.databinding

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 9/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.LiveData
import android.databinding.BindingAdapter
import android.view.View

object BindingView {

    @JvmStatic
    @BindingAdapter("visible")
    fun visible(view: View, visible: Boolean) = if (visible) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }

    @JvmStatic
    @BindingAdapter("liveVisible")
    fun liveVisible(view: View, liveVisible: LiveData<Boolean>) {
        liveVisible.observeForever { show ->
            if (show == true) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }
    }
}
