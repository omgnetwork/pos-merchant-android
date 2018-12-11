package network.omisego.omgmerchant.pages.authorized.loading

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 6/12/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.constant.enums.ErrorCode
import co.omisego.omisego.extension.bd
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.TransactionConsumption
import co.omisego.omisego.utils.GsonProvider
import com.google.gson.reflect.TypeToken
import com.nhaarman.mockito_kotlin.whenever
import network.omisego.omgmerchant.NavBottomNavigationDirections
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.helper.ResourceFile
import network.omisego.omgmerchant.helper.stringRes
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.repository.RemoteRepository
import org.amshove.kluent.mock
import org.amshove.kluent.shouldEqual
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23])
class LoadingViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val fileRejectedTransactionConsumption: File by ResourceFile("rejected_transaction_consumption.json")
    private val gson by lazy { GsonProvider.create() }

    private val responseRejectedTransactionConsumption by lazy {
        val txConsumptionToken = object : TypeToken<OMGResponse<TransactionConsumption>>() {}.type
        gson.fromJson<OMGResponse<TransactionConsumption>>(fileRejectedTransactionConsumption.readText(), txConsumptionToken)
    }

    private val viewModel: LoadingViewModel by lazy {
        LoadingViewModel(RemoteRepository()).apply {
            liveDirection = MutableLiveData()
        }
    }

    @Test
    fun `test handle rejected transaction consumption correctly`() {
        mockHandlerArg(viewModel)
        viewModel.liveCancelTransactionConsumption = mock()

        val data = responseRejectedTransactionConsumption.data
        viewModel.handleRejectTransactionConsumptionSuccess(data)

        viewModel.liveDirection.value?.peekContent().toString().trim() shouldEqual NavBottomNavigationDirections.ActionGlobalFeedbackFragment(
            Feedback.error(
                viewModel.confirmFragmentArgs!!,
                data.transactionRequest.address,
                data.transactionRequest.user,
                APIError(ErrorCode.SDK_UNEXPECTED_ERROR, stringRes(R.string.feedback_canceled))
            )
        ).toString().trim()
    }

    private fun mockHandlerArg(viewModel: LoadingViewModel) {
        viewModel.confirmFragmentArgs = mock()
        whenever(viewModel.confirmFragmentArgs?.amount).thenReturn("1000")
        whenever(viewModel.confirmFragmentArgs?.token).thenReturn(mock())
        whenever(viewModel.confirmFragmentArgs?.user).thenReturn(mock())
        whenever(viewModel.confirmFragmentArgs?.user?.id).thenReturn("user_id")
        whenever(viewModel.confirmFragmentArgs?.transactionType).thenReturn(SCAN_RECEIVE)
        whenever(viewModel.confirmFragmentArgs?.token?.subunitToUnit).thenReturn(100.bd)
        whenever(viewModel.confirmFragmentArgs?.token?.id).thenReturn("omg_id")
    }
}
