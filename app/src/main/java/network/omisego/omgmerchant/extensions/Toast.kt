package network.omisego.omgmerchant.extensions

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.widget.Toast

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

@SuppressLint("ShowToast")
fun Fragment.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toaster.toast?.cancel()
    Toaster.toast = Toast.makeText(context, msg, duration)
    Toaster.toast?.show()
}

object Toaster {
    var toast: Toast? = null
}
