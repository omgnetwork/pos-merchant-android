package network.omisego.omgmerchant.databinding

import android.databinding.BindingAdapter
import android.support.annotation.RawRes
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import network.omisego.omgmerchant.extensions.dpToPx

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
        val options = RequestOptions().transforms(
            CenterCrop(),
            RoundedCorners(view.context.dpToPx(8f))
        )
        Glide
            .with(view.context)
            .setDefaultRequestOptions(options)
            .load("https://api.adorable.io/avatars/214/$imageUrl.png").into(view)
    }

    @JvmStatic
    @BindingAdapter("loadGif")
    fun loadGif(view: ImageView, @RawRes imageRes: Int) {
        Glide
            .with(view.context)
            .load(imageRes)
            .into(view)
    }
}
