package network.omisego.omgmerchant.pages.main.receive

import android.arch.lifecycle.ViewModel
import network.omisego.omgmerchant.calculator.Calculation
import network.omisego.omgmerchant.calculator.CalculatorHandler
import network.omisego.omgmerchant.model.LiveCalculator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 7/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class ReceiveViewModel(
    val handler: CalculatorHandler = CalculatorHandler(),
    val liveCalculator: LiveCalculator = LiveCalculator("0"),
    val calculation: Calculation = Calculation()
) : ViewModel(), CalculatorHandler.Operation {

    /* Implement CalculatorHandler.Operation */
    override fun onAppend(char: CharSequence) {
        if (liveCalculator.value?.contains(".") == true && char == ".") return
        if (liveCalculator.value == "0") liveCalculator.value = ""
        liveCalculator.value += char
    }

    override fun onClear() {
        liveCalculator.value = "0"
    }

    override fun onDelete() {
        // Delete isn't available on the receive page.
    }

    override fun onEvaluate(): Boolean {
        val evaluated = calculation.evaluate(liveCalculator.value!!)
        if (evaluated == liveCalculator.value) return false
        liveCalculator.value = evaluated
        return true
    }

    init {
        handler.operation = this
    }
}
