package network.omisego.omgmerchant.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.support.v4.app.Fragment
import android.view.WindowManager

fun Fragment.enterFullscreen() {
    activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

fun Fragment.exitFullscreen() {
    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}
