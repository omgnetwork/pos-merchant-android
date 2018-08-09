package network.omisego.omgmerchant.calculator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 7/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

enum class CalculatorButton(val sign: CharSequence) {
    NUM_0("0"),
    NUM_1("1"),
    NUM_2("2"),
    NUM_3("3"),
    NUM_4("4"),
    NUM_5("5"),
    NUM_6("6"),
    NUM_7("7"),
    NUM_8("8"),
    NUM_9("9"),
    OP_PERCENT("%"),
    OP_PLUS("+"),
    OP_MINUS("-"),
    OP_EQUAL("="),
    OP_DOT("."),
    OP_DEL("del"),
    OP_AC("AC");

    companion object {
        fun from(sign: CharSequence): CalculatorButton? =
            CalculatorButton.values().firstOrNull { it.sign == sign }

        fun isOperation(button: CalculatorButton) = button in arrayOf(OP_MINUS, OP_PLUS, OP_DOT, OP_PERCENT)
    }
}
