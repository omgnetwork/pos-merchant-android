package network.omisego.omgmerchant.helper

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import org.robolectric.RuntimeEnvironment
import java.io.File
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

fun stringRes(@StringRes id: Int) = RuntimeEnvironment.application.getString(id)

fun drawableRes(@DrawableRes id: Int) = RuntimeEnvironment.application.getDrawable(id)

fun colorRes(@ColorRes id: Int) = RuntimeEnvironment.application.getColor(id)

class ResourceFile(private val fileName: String) : ReadOnlyProperty<Any, File> {
    override fun getValue(thisRef: Any, property: KProperty<*>): File {
        return File(javaClass.classLoader.getResource("object/$fileName").path)
    }
}
