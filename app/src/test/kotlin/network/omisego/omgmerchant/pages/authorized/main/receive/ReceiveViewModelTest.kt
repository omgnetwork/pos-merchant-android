package network.omisego.omgmerchant.pages.authorized.main.receive

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 2/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import org.amshove.kluent.mock
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ReceiveViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val liveCalculator: MutableLiveData<String> by lazy { MutableLiveData<String>().apply { this.value = "" } }
    private val viewModel: ReceiveViewModel by lazy { ReceiveViewModel(mock(), mock(), mock()) }

    @Test
    fun `test next button enable state`() {
        // Should not enable next
        liveCalculator.value = "0"
        viewModel.shouldEnableNextButton() shouldBe false

        liveCalculator.value = "1-"
        viewModel.shouldEnableNextButton() shouldBe false

        liveCalculator.value = "1+"
        viewModel.shouldEnableNextButton() shouldBe false

        liveCalculator.value = "-1"
        viewModel.shouldEnableNextButton() shouldBe false

        // Should enable next
        liveCalculator.value = "1"
        viewModel.shouldEnableNextButton() shouldBe true
    }

    @Test
    fun `test calculator should not be able to have multiple dots`() {
        liveCalculator.value = "1.1"
        viewModel.onAppend('.')
        liveCalculator.value shouldEqual "1.1"
    }

    @Test
    fun `test calculator should not be able to add multiple zeroes without non-zero value in the left-most digit`() {
        /* Should not be able to add */
        liveCalculator.value = "0"
        viewModel.onAppend('0')
        liveCalculator.value shouldEqual '0'

        /* Should be ok */
        liveCalculator.value = "10"
        viewModel.onAppend('0')
        liveCalculator.value shouldEqual "100"

        liveCalculator.value = "0."
        viewModel.onAppend('0')
        liveCalculator.value shouldEqual "0.0"
    }
}
