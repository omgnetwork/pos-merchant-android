package network.omisego.omgmerchant.pages.main.topup

import android.arch.lifecycle.ViewModel
import network.omisego.omgmerchant.calculator.CalculatorHandler
import network.omisego.omgmerchant.model.LiveCalculator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 9/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class TopupViewModel(
    val handler: CalculatorHandler = CalculatorHandler(),
    val liveCalculator: LiveCalculator = LiveCalculator()
) : ViewModel(), CalculatorHandler.Operation {
    override fun onAppend(char: CharSequence) {
        if (liveCalculator.value?.contains(".") == true && char == ".") return
        liveCalculator.value += char
    }

    override fun onDelete() {
        liveCalculator.value = liveCalculator.value?.dropLast(1)
    }

    override fun onClear() {
        // Clear isn't available on the topup page.
    }

    // Evaluate isn't available on the topup page.
    override fun onEvaluate(): Boolean = false

    init {
        handler.operation = this
    }
}