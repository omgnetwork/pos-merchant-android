package network.omisego.omgmerchant.calculator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 7/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import network.omisego.omgmerchant.calculator.CalculatorButton.NUM_0
import network.omisego.omgmerchant.calculator.CalculatorButton.NUM_1
import network.omisego.omgmerchant.calculator.CalculatorButton.NUM_2
import network.omisego.omgmerchant.calculator.CalculatorButton.NUM_3
import network.omisego.omgmerchant.calculator.CalculatorButton.NUM_4
import network.omisego.omgmerchant.calculator.CalculatorButton.NUM_5
import network.omisego.omgmerchant.calculator.CalculatorButton.NUM_6
import network.omisego.omgmerchant.calculator.CalculatorButton.NUM_7
import network.omisego.omgmerchant.calculator.CalculatorButton.NUM_8
import network.omisego.omgmerchant.calculator.CalculatorButton.NUM_9
import network.omisego.omgmerchant.calculator.CalculatorButton.OP_AC
import network.omisego.omgmerchant.calculator.CalculatorButton.OP_DOT
import network.omisego.omgmerchant.calculator.CalculatorButton.OP_EQUAL
import network.omisego.omgmerchant.calculator.CalculatorButton.OP_MINUS
import network.omisego.omgmerchant.calculator.CalculatorButton.OP_PERCENT
import network.omisego.omgmerchant.calculator.CalculatorButton.OP_PLUS

class CalculatorInteraction(
    private val condition: CalculatorCondition = CalculatorCondition(
        CalculatorState(false, false, NUM_0)
    )
) {
    var operation: Operation? = null

    fun handleNumPadPressed(view: View) {
        val callback = operation ?: return
        if (view is TextView) {
            val button = CalculatorButton.from(view.text) ?: return
            if (condition.isOperatorDuplicated(button)) return
            when (button) {
                NUM_0, NUM_1, NUM_2, NUM_3, NUM_4, NUM_5, NUM_6, NUM_7, NUM_8, NUM_9, OP_DOT -> {
                    with(condition) {
                        shouldClearBeforeAppend { operation?.onClear() }
                        dispatch(button) {
                            callback.onAppend(it[0])
                        }
                    }
                }
                OP_AC -> {
                    condition.handleAC { callback.onClear() }
                }
                OP_PLUS,
                OP_MINUS -> {
                    with(condition) {
                        handlePlusMinusEvaluation(callback::onEvaluate) {
                            dispatch(button) {
                                callback.onAppend(it[0])
                            }
                        }
                    }
                }
                OP_PERCENT -> {
                    condition.evaluatePercent {
                        callback.onAppend(it.sign[0])
                        callback.onEvaluate()
                    }
                }
                OP_EQUAL -> {
                    condition.evaluateEqual {
                        callback.onEvaluate()
                    }
                }
            }
        } else if (view is ImageButton) {
            callback.onDelete()
        }
    }

    interface Operation {
        fun onAppend(char: Char)
        fun onClear()
        fun onDelete()
        fun onEvaluate(): Boolean
    }
}
