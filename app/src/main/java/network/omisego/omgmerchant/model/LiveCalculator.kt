package network.omisego.omgmerchant.model

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 7/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData

class LiveCalculator : MutableLiveData<String>() {
    init {
        value = ""
    }
}
