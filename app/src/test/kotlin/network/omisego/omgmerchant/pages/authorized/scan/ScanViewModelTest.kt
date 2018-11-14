package network.omisego.omgmerchant.pages.authorized.scan

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 2/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.whenever
import network.omisego.omgmerchant.R
import org.amshove.kluent.mock
import org.amshove.kluent.shouldEqualTo
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23])
class ScanViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val viewModel: ScanViewModel by lazy { ScanViewModel(RuntimeEnvironment.application) }
    private val mockArgs: ScanFragmentArgs by lazy { mock<ScanFragmentArgs>() }

    @Test
    fun `test amountText should be displayed correctly`() {
        viewModel.args = mockArgs
        whenever(mockArgs.amount).thenReturn("100")
        whenever(mockArgs.token).thenReturn(mock())
        whenever(mockArgs.token.symbol).thenReturn("OMG")

        viewModel.amountText shouldEqualTo "100.00 OMG"
    }

    @Test
    fun `test title should be displayed correctly`() {
        viewModel.args = mockArgs

        whenever(mockArgs.transactionType).thenReturn(SCAN_RECEIVE)
        viewModel.title shouldEqualTo RuntimeEnvironment.application.getString(R.string.scan_title_payment)

        whenever(mockArgs.transactionType).thenReturn(SCAN_TOPUP)
        viewModel.title shouldEqualTo RuntimeEnvironment.application.getString(R.string.scan_title_topup)
    }
}
