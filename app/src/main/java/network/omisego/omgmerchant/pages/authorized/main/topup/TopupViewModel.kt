package network.omisego.omgmerchant.pages.authorized.main.topup

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 9/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.model.Token
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.calculator.CalculatorInteraction
import network.omisego.omgmerchant.helper.HelperFormatter
import network.omisego.omgmerchant.pages.authorized.main.AbstractCalculatorController
import java.math.BigDecimal

class TopupViewModel(
    val app: Application,
    val handler: CalculatorInteraction
) : AndroidViewModel(app), CalculatorInteraction.Operation, AbstractCalculatorController {
    override val formatter: HelperFormatter by lazy { HelperFormatter() }
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
        val calculatorValue = liveCalculator.value
        return !calculatorValue.isNullOrEmpty() &&
            calculatorValue.toDouble() != 0.0 &&
            liveCalculatorHelperText.value != app.getString(R.string.calculator_helper_exceed_maximum)
    }

    override fun dispatchHelperTextState(calculatorText: String?, subunitToUnit: BigDecimal?) {
        try {
            val decimal = calculatorText?.toBigDecimal()?.scale() ?: 0
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