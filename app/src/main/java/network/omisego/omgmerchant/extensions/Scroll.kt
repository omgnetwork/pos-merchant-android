package network.omisego.omgmerchant.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import network.omisego.omgmerchant.MainActivity
import network.omisego.omgmerchant.R

fun AppCompatActivity.scrollBottom() {
    if (this is MainActivity) {
        val scroller = this.findViewById<NestedScrollView>(R.id.nestedScrollView)
        scroller?.scrollTo(0, scroller.bottom)
    }
}

fun Fragment.scrollBottom() {
    val hostActivity = activity
    if (hostActivity is AppCompatActivity) {
        hostActivity.scrollBottom()
    }
}
