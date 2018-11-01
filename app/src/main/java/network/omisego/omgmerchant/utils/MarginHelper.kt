package network.omisego.omgmerchant.utils

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 1/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Context
import android.graphics.Rect
import network.omisego.omgmerchant.extensions.dpToPx

fun Context.provideMarginLeft(marginLeft: Int = 96): Rect {
    return Rect(dpToPx(marginLeft.toFloat()), 0, 0, 0)
}

