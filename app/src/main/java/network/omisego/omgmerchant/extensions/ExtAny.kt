package network.omisego.omgmerchant.extensions

import android.util.Log

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 14/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

fun Any.logi(message: Any?) = Log.i(this.javaClass.simpleName, message?.toString())

fun Any.logd(message: Any?) = Log.d(this.javaClass.simpleName, message?.toString())
