package network.omisego.omgmerchant.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import network.omisego.omgmerchant.MainActivity
import network.omisego.omgmerchant.R

fun Fragment.findChildController(@IdRes id: Int = R.id.navBottomNavigationContainer): NavController {
    return Navigation.findNavController(activity as MainActivity, id)
}

fun Fragment.findRootController(@IdRes id: Int = R.id.nav_host): NavController {
    return Navigation.findNavController(activity as MainActivity, id)
}
