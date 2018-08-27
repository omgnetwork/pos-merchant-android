package network.omisego.omgmerchant.pages.main

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 15/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import network.omisego.omgmerchant.extensions.mutableLiveDataOf

class MainViewModel : ViewModel() {
    val liveEnableNext: MutableLiveData<Boolean> by lazy { mutableLiveDataOf(false) }
}
