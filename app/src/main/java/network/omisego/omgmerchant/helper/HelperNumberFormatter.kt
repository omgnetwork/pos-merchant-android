package network.omisego.omgmerchant.helper

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 9/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class HelperNumberFormatter {
    fun formatByNumber(number: String): String {
        return try {
            val split = number.split(".")
            return if (split.size > 1 && split[1].isNotEmpty()) {
                ".${split[1].length}f"
                String.format("%,.${split[1].length}f", number.toBigDecimal())
            } else if (split.size > 1) {
                String.format("%,d", split[0].toBigInteger()) + "."
            } else {
                String.format("%,d", number.toBigInteger())
            }
        } catch (e: Throwable) {
            number
        }
    }

    fun formatByExpression(expression: String): String {
        val operatorIndex = expression.indexOfAny(charArrayOf('+', '-'))
        if (operatorIndex == -1) return formatByNumber(expression)
        val numbers = splitOperator(expression).filterNot { it.isEmpty() }
        return numbers.fold(expression) { acc, number ->
            acc.replace(number, formatByNumber(number))
        }
    }

    fun splitOperator(expression: String): List<String> {
        return expression.split(Regex("[+-]"))
    }
}