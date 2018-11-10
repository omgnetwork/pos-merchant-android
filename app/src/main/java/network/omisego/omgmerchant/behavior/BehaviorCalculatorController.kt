package network.omisego.omgmerchant.behavior

import android.arch.lifecycle.MutableLiveData
import co.omisego.omisego.model.Token
import network.omisego.omgmerchant.helper.HelperNumberFormatter

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 2/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

interface BehaviorCalculatorController {
    val helperNumberFormatter: HelperNumberFormatter
    val liveSelectedToken: MutableLiveData<Token>
    val liveCalculator: MutableLiveData<String>
    val liveCalculatorShowHelperText: MutableLiveData<Boolean>
    val liveCalculatorHelperText: MutableLiveData<String>
    val liveCalculatorHelperColorText: MutableLiveData<Int>

    fun dispatchHelperTextState()

    fun shouldEnableNextButton(): Boolean
}