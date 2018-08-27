package network.omisego.omgmerchant.calculator

import network.omisego.omgmerchant.calculator.CalculatorButton.Companion.isOperation
import network.omisego.omgmerchant.calculator.CalculatorButton.NUM_0
import network.omisego.omgmerchant.calculator.CalculatorButton.OP_PERCENT

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 9/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class CalculatorCondition(
    val state: CalculatorState
) {
    inline fun shouldClearBeforeAppend(lambda: () -> Unit) {
        if (state.clearBeforeAppend) {
            state.clearBeforeAppend = false
            lambda.invoke()
        }
    }

    inline fun dispatch(button: CalculatorButton, lambda: (CharSequence) -> Unit) {
        state.recentButton = button
        lambda(button.sign)
    }

    inline fun evaluateEqual(evaluate: () -> Boolean) {
        if (evaluate()) {
            state.clearBeforeAppend = true
        }
    }

    inline fun evaluatePercent(evaluate: (CalculatorButton) -> Boolean) {
        if (isOperatorDuplicated(OP_PERCENT)) return
        if (evaluate(OP_PERCENT)) {
            state.clearBeforeAppend = true
        }
    }

    inline fun handlePlusMinusEvaluation(evaluate: () -> Boolean, append: () -> Unit) {
        if (state.containOperator) {
            evaluate()
        }
        append()
        state.containOperator = true
        state.clearBeforeAppend = false
    }

    inline fun handleAC(clear: () -> Unit) {
        clear()
        state.clearBeforeAppend = false
        state.containOperator = false
        state.recentButton = NUM_0
    }

    fun isOperatorDuplicated(button: CalculatorButton) = isOperation(state.recentButton) && isOperation(button)
}
