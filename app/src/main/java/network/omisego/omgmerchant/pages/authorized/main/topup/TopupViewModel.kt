package network.omisego.omgmerchant.pages.authorized.main.topup

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 9/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.support.v4.content.ContextCompat
import co.omisego.omisego.model.Token
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.behavior.BehaviorCalculatorController
import network.omisego.omgmerchant.calculator.CalculatorInteraction
import network.omisego.omgmerchant.helper.HelperNumberFormatter

class TopupViewModel(
    val app: Application,
    val handler: CalculatorInteraction
) : AndroidViewModel(app), CalculatorInteraction.Operation, BehaviorCalculatorController {
    override val helperNumberFormatter: HelperNumberFormatter by lazy { HelperNumberFormatter() }
    override val liveSelectedToken: MutableLiveData<Token> by lazy { MutableLiveData<Token>() }
    override val liveCalculator: MutableLiveData<String> by lazy { MutableLiveData<String>().apply { this.value = "0" } }
    override val liveCalculatorShowHelperText: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val liveCalculatorHelperText: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val liveCalculatorHelperColorText: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    override fun onAppend(char: Char) {
        if (char in '0'..'9' && liveCalculatorShowHelperText.value == true) return
        if (liveCalculator.value?.contains('.') == true && char == '.') return
        if (liveCalculator.value == "0" && char != '.') liveCalculator.value = ""
        liveCalculator.value += char
    }

    override fun onDelete() {
        liveCalculator.value = liveCalculator.value?.dropLast(1)
        if (liveCalculator.value?.isEmpty() == true) {
            liveCalculator.value = "0"
        }
    }

    override fun onClear() {
        // Clear isn't available on the topup page.
    }

    // Evaluate isn't available on the topup page.
    override fun onEvaluate(): Boolean = false

    override fun shouldEnableNextButton(): Boolean {
        return liveCalculator.value != "0"
            && liveCalculatorHelperText.value != app.getString(R.string.calculator_helper_exceed_maximum)
    }

    override fun dispatchHelperTextState() {
        try {
            val calculatorValue = liveCalculator.value
            val subunitToUnit = liveSelectedToken.value?.subunitToUnit
            val decimal = calculatorValue?.toBigDecimal()?.scale() ?: 0
            val maxDecimal = Math.log10(subunitToUnit?.toDouble() ?: 10.0).toInt()

            when {
                decimal > maxDecimal -> {
                    liveCalculatorHelperColorText.value = ContextCompat.getColor(app, R.color.colorRed)
                    liveCalculatorHelperText.value = app.getString(R.string.calculator_helper_exceed_maximum)
                    liveCalculatorShowHelperText.value = true
                }
                decimal == maxDecimal -> {
                    liveCalculatorHelperColorText.value = ContextCompat.getColor(app, R.color.colorGrayWeak)
                    liveCalculatorHelperText.value = app.getString(R.string.calculator_helper_reach_maximum)
                    liveCalculatorShowHelperText.value = true
                }
                else -> {
                    liveCalculatorShowHelperText.value = false
                    liveCalculatorHelperText.value = ""
                }
            }
        } catch (e: Exception) {
            liveCalculatorShowHelperText.value = false
        }
    }

    init {
        handler.operation = this
    }
}