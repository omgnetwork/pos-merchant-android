package network.omisego.omgmerchant.calculator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 9/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.junit.Test

class CalculationTest {
    private val calculation: Calculation by lazy { Calculation() }

    @Test
    fun `evaluate simple expression should be correct`() {
        calculation.evaluate("5-2") shouldEqualTo "3"
        calculation.evaluate("5+1") shouldEqualTo "6"
        calculation.evaluate("1-2") shouldEqualTo "-1"
        calculation.evaluate("+1-2") shouldEqualTo "-1"
        calculation.evaluate("+2-1") shouldEqualTo "1"
    }

    @Test
    fun `evaluate should calculate less than zero value successfully`() {
        calculation.evaluate("-20+39") shouldEqualTo "19"
        calculation.evaluate("-20-39") shouldEqualTo "-59"
    }

    @Test
    fun `evaluate fraction number should be success`() {
        calculation.evaluate("0.1+0.2") shouldEqualTo "0.3"
        calculation.evaluate("0.5+0.5") shouldEqualTo "1.0"
        calculation.evaluate("5.95-0.45") shouldEqualTo "5.50"
        calculation.evaluate("-5.95-0.45") shouldEqualTo "-6.40"
    }

    @Test
    fun `evaluate expression without operand should be success`() {
        calculation.evaluate("6+") shouldEqualTo "6+"
    }

    @Test
    fun `evaluate percent should be correct`(){
        calculation.evaluate("+100%") shouldEqualTo "1"
        calculation.evaluate("-100%") shouldEqualTo "-1"
        calculation.evaluate("1+10%") shouldEqualTo "1.1"
        calculation.evaluate("1-10%") shouldEqualTo "0.9"
    }

    @Test
    fun `split should be able to split expression successfully`() {
        calculation.split("1+2", charArrayOf('+')) shouldEqual ('+' to ("1" to "2"))
        calculation.split("1-2", charArrayOf('-')) shouldEqual ('-' to ("1" to "2"))
        calculation.split("1-2%", charArrayOf('-')) shouldEqual ('-' to ("1" to "2%"))
        calculation.split("2%", charArrayOf('+', '-')) shouldEqual ('%' to ("2" to "0"))
        calculation.split("+2%", charArrayOf('+', '-')) shouldEqual ('%' to ("+2" to "0"))
        calculation.split("-2%", charArrayOf('+', '-')) shouldEqual ('%' to ("-2" to "0"))
    }
}
