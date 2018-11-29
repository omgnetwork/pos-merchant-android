package network.omisego.omgmerchant.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.annotation.SuppressLint
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import network.omisego.omgmerchant.AndroidViewModelFactory
import network.omisego.omgmerchant.MainActivity
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.ViewModelFactory
import network.omisego.omgmerchant.pages.authorized.main.MainFragment

fun Fragment.findChildController(@IdRes id: Int = R.id.navBottomNavigationContainer): NavController {
    return Navigation.findNavController(activity as MainActivity, id)
}

fun Fragment.findRootController(@IdRes id: Int = R.id.nav_host): NavController {
    return Navigation.findNavController(activity as MainActivity, id)
}

fun Fragment.findMainFragment(): MainFragment {
    var parent: Fragment? = this
    while (parent !is MainFragment) {
        parent = parent?.parentFragment
        if (parent == null) throw NullPointerException()
    }
    return parent
}

inline fun <reified T : ViewModel> Fragment.provideActivityViewModel(): T {
    return ViewModelProviders.of(activity!!, ViewModelFactory())[T::class.java]
}

inline fun <reified T : ViewModel> Fragment.provideMainFragmentViewModel(): T {
    var parent: Fragment? = this
    while (parent != null) {
        if (parent is MainFragment)
            return ViewModelProviders.of(parent, ViewModelFactory())[T::class.java]
        parent = parent.parentFragment
    }
    throw IllegalStateException("The specified fragment is not a child of MainFragment.")
}

inline fun <reified T : AndroidViewModel> Fragment.provideMainFragmentAndroidViewModel(): T {
    var parent: Fragment? = this
    while (parent != null) {
        if (parent is MainFragment)
            return ViewModelProviders.of(parent, AndroidViewModelFactory(this.activity!!.application))[T::class.java]
        parent = parent.parentFragment
    }
    throw IllegalStateException("The specified fragment is not a child of MainFragment.")
}

inline fun <reified T : AndroidViewModel> Fragment.provideActivityAndroidViewModel(): T {
    return ViewModelProviders.of(activity!!, AndroidViewModelFactory(this.activity!!.application))[T::class.java]
}

inline fun <reified T : ViewModel> Fragment.provideViewModel(): T {
    return ViewModelProviders.of(this, ViewModelFactory())[T::class.java]
}

inline fun <reified T : AndroidViewModel> Fragment.provideAndroidViewModel(): T {
    return ViewModelProviders.of(this, AndroidViewModelFactory(this.activity!!.application))[T::class.java]
}

@SuppressLint("ShowToast")
fun Fragment.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toaster.toast?.cancel()
    Toaster.toast = Toast.makeText(context, msg, duration)
    Toaster.toast?.show()
}

object Toaster {
    var toast: Toast? = null
}
