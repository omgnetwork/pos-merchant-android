package network.omisego.omgmerchant.pages.authorized.main

import android.arch.lifecycle.MutableLiveData
import co.omisego.omisego.model.Token
import network.omisego.omgmerchant.helper.HelperNumberFormatter
import java.math.BigDecimal

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 2/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

interface AbstractCalculatorController {
    val helperNumberFormatter: HelperNumberFormatter
    val liveSelectedToken: MutableLiveData<Token>
    val liveCalculator: MutableLiveData<String>
    val liveCalculatorShowHelperText: MutableLiveData<Boolean>
    val liveCalculatorHelperText: MutableLiveData<String>
    val liveCalculatorHelperColorText: MutableLiveData<Int>

    fun dispatchHelperTextState(
        calculatorText: String? = liveCalculator.value,
        subunitToUnit: BigDecimal? = liveSelectedToken.value?.subunitToUnit
    )

    fun shouldEnableNextButton(): Boolean
}