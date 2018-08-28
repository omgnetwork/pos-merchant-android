package network.omisego.omgmerchant.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import network.omisego.omgmerchant.R

fun Fragment.replaceFragment(
    @IdRes layoutContainerRes: Int = R.id.pageContainer,
    fragment: Fragment
) {
    childFragmentManager
        .beginTransaction()
        .replace(layoutContainerRes, fragment)
        .commit()
}

fun Fragment.replaceFragmentBackstack(
    @IdRes layoutContainerRes: Int = R.id.pageContainer,
    fragment: Fragment
) {
    childFragmentManager
        .beginTransaction()
        .addToBackStack(null)
        .replace(layoutContainerRes, fragment)
        .commit()
}
