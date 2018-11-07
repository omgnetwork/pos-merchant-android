package network.omisego.omgmerchant.pages.authorized.main.receive

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 7/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.Token
import network.omisego.omgmerchant.calculator.Calculation
import network.omisego.omgmerchant.calculator.CalculatorInteraction
import network.omisego.omgmerchant.model.LiveCalculator
import network.omisego.omgmerchant.pages.authorized.main.NextButtonBehavior
import network.omisego.omgmerchant.utils.NumberDecorator

class ReceiveViewModel(
    val handler: CalculatorInteraction,
    val liveCalculator: LiveCalculator,
    private val calculation: Calculation
) : ViewModel(), CalculatorInteraction.Operation, NextButtonBehavior {
    val liveSelectedToken: MutableLiveData<Token> by lazy { MutableLiveData<Token>() }
    val numberDecorator: NumberDecorator by lazy { NumberDecorator() }

    /* Implement CalculatorInteraction.Operation */
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

    override fun shouldEnableNextButton(): Boolean {
        val calculatorValue = liveCalculator.value
        return calculatorValue != "0" && calculatorValue?.indexOfAny(charArrayOf('-', '+')) == -1
    }

    init {
        handler.operation = this
    }
}
