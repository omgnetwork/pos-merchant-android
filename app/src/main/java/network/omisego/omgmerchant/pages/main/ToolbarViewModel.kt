package network.omisego.omgmerchant.pages.main

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import network.omisego.omgmerchant.extensions.mutableLiveDataOf

class ToolbarViewModel(
    val app: Application
) : AndroidViewModel(app) {
    val liveToolbarText: MutableLiveData<String> by lazy { mutableLiveDataOf("") }
}