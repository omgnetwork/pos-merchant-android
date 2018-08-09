@file:Suppress("IMPLICIT_CAST_TO_ANY")

package network.omisego.omgmerchant.calculator

import co.omisego.omisego.extension.bd
import network.omisego.omgmerchant.model.Equation
import java.math.BigDecimal

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 8/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class Calculation {
    fun convert(left: String, operator: String, right: String): Equation {
        val last = left.toBigDecimal()
        val percentRightIfNeeded = if (right.last() == '%') {
            val percent = right.substringBefore('%').toBigDecimal()
            calculatePercent(last, percent)
        } else {
            right.toBigDecimal()
        }
        return Equation(last, CalculatorButton.from(operator)!!, percentRightIfNeeded)
    }

    fun calculate(equation: Equation): String {
        val (left, operator, right) = equation
        return when (operator) {
            CalculatorButton.OP_MINUS -> left.minus(right)
            CalculatorButton.OP_PLUS -> left.plus(right)
            CalculatorButton.OP_PERCENT -> left.divide(100.bd)
            else -> throw UnsupportedOperationException("Unsupported operation $operator")
        }.toPlainString()
    }

    fun split(expression: String, operators: CharArray): Pair<Char, Pair<String, String>>? {
        val operatorIndex = expression.lastIndexOfAny(operators)
        if (operatorIndex <= 0 || operatorIndex == expression.length - 1) {
            if (expression.last() == '%' && operatorIndex <= 0) {
                return '%' to (expression.substring(0, expression.length - 1) to "0")
            }
            return null
        }
        val operator = expression[operatorIndex]

        val left = expression.substring(0, operatorIndex)
        val right = expression.substring(operatorIndex + 1)

        return operator to (left to right)
    }

    fun calculatePercent(base: BigDecimal, percent: BigDecimal): BigDecimal {
        return base.multiply(percent).divide(100.bd)
    }

    fun evaluate(expression: String): String {
        /* Split the expression into pairs */
        if (expression == "%") return ""
        val elements = split(expression, charArrayOf('-', '+')) ?: return expression
        val operator = elements.first
        val (left, right) = elements.second
        val equation = convert(left, "$operator", right)
        return calculate(equation)
    }
}
