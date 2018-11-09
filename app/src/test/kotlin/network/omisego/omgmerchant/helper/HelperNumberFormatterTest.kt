package network.omisego.omgmerchant.helper

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 9/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.junit.Test

class HelperNumberFormatterTest {
    private val formatterHelper: HelperNumberFormatter by lazy { HelperNumberFormatter() }
    @Test
    fun `formatByNumber should be correct`() {
        formatterHelper.formatByNumber("100000") shouldEqualTo "100,000"
        formatterHelper.formatByNumber("200000.53") shouldEqualTo "200,000.53"
        formatterHelper.formatByNumber("100") shouldEqualTo "100"
        formatterHelper.formatByNumber("55555555555") shouldEqualTo "55,555,555,555"
        formatterHelper.formatByNumber("5555555555555555555555") shouldEqualTo "5,555,555,555,555,555,555,555"
    }

    @Test
    fun `formatByExpression should be correct`() {
        formatterHelper.formatByExpression("1234+4321") shouldEqualTo "1,234+4,321"
        formatterHelper.formatByExpression("1-666666666666666666666") shouldEqualTo "1-666,666,666,666,666,666,666"
    }

    @Test
    fun `splitOperator should be correct`() {
        formatterHelper.splitOperator("1234+4321") shouldEqual listOf("1234", "4321")
        formatterHelper.splitOperator("1234-4321") shouldEqual listOf("1234", "4321")
        formatterHelper.splitOperator("-1234+4321") shouldEqual listOf("", "1234", "4321")
        formatterHelper.splitOperator("+1234-4321") shouldEqual listOf("", "1234", "4321")
    }
}