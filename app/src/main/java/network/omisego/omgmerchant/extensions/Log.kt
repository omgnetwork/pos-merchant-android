package network.omisego.omgmerchant.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
import android.support.v4.app.Fragment
import android.util.Log

fun Fragment.logi(message: Any?) = Log.i(this.javaClass.name, message?.toString())

fun Fragment.logd(message: Any?) = Log.d(this.javaClass.name, message?.toString())
