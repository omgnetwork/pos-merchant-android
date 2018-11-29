package network.omisego.omgmerchant.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 7/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.getDrawableCompat(@DrawableRes res: Int) = ContextCompat.getDrawable(this, res)
