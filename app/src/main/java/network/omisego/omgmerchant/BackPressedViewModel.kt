package network.omisego.omgmerchant

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 28/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class BackPressedViewModel : ViewModel() {
    val liveBackPressed: MutableLiveData<String> = MutableLiveData()
    val liveOnBackPressed: MutableLiveData<() -> Boolean> = MutableLiveData()
}
