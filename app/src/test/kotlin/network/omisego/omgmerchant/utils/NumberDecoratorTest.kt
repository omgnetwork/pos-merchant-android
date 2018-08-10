package network.omisego.omgmerchant.utils

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 9/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.junit.Test

class NumberDecoratorTest {
    private val decorator: NumberDecorator by lazy { NumberDecorator() }
    @Test
    fun `formatByNumber should be correct`() {
        decorator.formatByNumber("100000") shouldEqualTo "100,000"
        decorator.formatByNumber("200000.53") shouldEqualTo "200,000.53"
        decorator.formatByNumber("100") shouldEqualTo "100"
        decorator.formatByNumber("55555555555") shouldEqualTo "55,555,555,555"
        decorator.formatByNumber("5555555555555555555555") shouldEqualTo "5,555,555,555,555,555,555,555"
    }

    @Test
    fun `formatByExpression should be correct`() {
        decorator.formatByExpression("1234+4321") shouldEqualTo "1,234+4,321"
        decorator.formatByExpression("1-666666666666666666666") shouldEqualTo "1-666,666,666,666,666,666,666"
    }

    @Test
    fun `splitOperator should be correct`() {
        decorator.splitOperator("1234+4321") shouldEqual listOf("1234", "4321")
        decorator.splitOperator("1234-4321") shouldEqual listOf("1234", "4321")
        decorator.splitOperator("-1234+4321") shouldEqual listOf("", "1234", "4321")
        decorator.splitOperator("+1234-4321") shouldEqual listOf("", "1234", "4321")
    }
}