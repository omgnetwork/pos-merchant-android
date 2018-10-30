package network.omisego.omgmerchant.pages.authorized.main

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import network.omisego.omgmerchant.extensions.mutableLiveDataOf

class ToolbarViewModel(
    val app: Application
) : AndroidViewModel(app) {
    private val liveToolbarTitle: MutableLiveData<String> by lazy { mutableLiveDataOf("") }

    fun setToolbarTitle(text: String) {
        liveToolbarTitle.value = text
    }

    fun getLiveToolbarTitle(): LiveData<String> = liveToolbarTitle
}
