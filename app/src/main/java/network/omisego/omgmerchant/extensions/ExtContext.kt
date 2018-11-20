package network.omisego.omgmerchant.extensions

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

fun Context.dpToPx(dp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
}

fun Context.provideMarginLeft(marginLeft: Int = 96): Rect {
    return Rect(dpToPx(marginLeft.toFloat()), 0, 0, 0)
}
