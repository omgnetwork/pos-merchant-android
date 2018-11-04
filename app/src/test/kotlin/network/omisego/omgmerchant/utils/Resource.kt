package network.omisego.omgmerchant.utils

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import org.robolectric.RuntimeEnvironment

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

fun stringRes(@StringRes id: Int) = RuntimeEnvironment.application.getString(id)

fun drawableRes(@DrawableRes id: Int) = RuntimeEnvironment.application.getDrawable(id)
