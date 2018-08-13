package network.omisego.omgmerchant.databinding

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object ImageViewUtil {
    @JvmStatic
    @BindingAdapter("loadImage")
    fun loadImage(view: ImageView, imageUrl: String) {
        Glide.with(view.context).load("https://api.adorable.io/avatars/214/$imageUrl.png").into(view)
    }
}
