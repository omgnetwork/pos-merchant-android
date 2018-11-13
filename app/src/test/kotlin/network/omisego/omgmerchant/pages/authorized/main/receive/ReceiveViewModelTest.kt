package network.omisego.omgmerchant.pages.authorized.main.receive

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 2/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.core.executor.testing.InstantTaskExecutorRule
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.helper.stringRes
import org.amshove.kluent.mock
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23])
class ReceiveViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val viewModel: ReceiveViewModel by lazy { ReceiveViewModel(RuntimeEnvironment.application, mock(), mock()) }

    @Before
    fun setup() {
        viewModel.liveCalculatorHelperText.value = "abcd"
    }

    @Test
    fun `test next button enable state`() {
        // Should not enable next
        viewModel.liveCalculator.value = "0"
        viewModel.shouldEnableNextButton() shouldBe false

        viewModel.liveCalculator.value = "1-"
        viewModel.shouldEnableNextButton() shouldBe false

        viewModel.liveCalculator.value = "1+"
        viewModel.shouldEnableNextButton() shouldBe false

        viewModel.liveCalculator.value = "-1"
        viewModel.shouldEnableNextButton() shouldBe false

        // Should enable next
        viewModel.liveCalculator.value = "1"
        viewModel.shouldEnableNextButton() shouldBe true

        viewModel.liveCalculator.value = "1"
        viewModel.liveCalculatorHelperText.value = stringRes(R.string.calculator_helper_exceed_maximum)
        viewModel.shouldEnableNextButton() shouldEqual false
    }

    @Test
    fun `test calculator should not be able to have multiple dots`() {
        viewModel.liveCalculator.value = "1.1"
        viewModel.onAppend('.')
        viewModel.liveCalculator.value shouldEqual "1.1"

        // Should be ok
        viewModel.liveCalculator.value = "100.24 + 100"
        viewModel.onAppend('.')
        viewModel.liveCalculator.value = "100.24 + 100."
    }

    @Test
    fun `test calculator should not be able to add multiple zeroes without non-zero value in the left-most digit`() {
        /* Should not be able to add */
        viewModel.liveCalculator.value = "0"
        viewModel.onAppend('0')
        viewModel.liveCalculator.value shouldEqual "0"

        /* Should be ok */
        viewModel.liveCalculator.value = "10"
        viewModel.onAppend('0')
        viewModel.liveCalculator.value shouldEqual "100"

        viewModel.liveCalculator.value = "0."
        viewModel.onAppend('0')
        viewModel.liveCalculator.value shouldEqual "0.0"
    }
}
