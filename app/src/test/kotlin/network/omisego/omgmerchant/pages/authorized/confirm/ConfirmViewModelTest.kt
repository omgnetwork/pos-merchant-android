package network.omisego.omgmerchant.pages.authorized.confirm

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 2/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.view.View
import co.omisego.omisego.extension.bd
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.User
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.model.params.WalletParams
import co.omisego.omisego.model.transaction.Transaction
import co.omisego.omisego.model.transaction.TransactionSource
import co.omisego.omisego.model.transaction.send.TransactionCreateParams
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.data.LocalRepository
import network.omisego.omgmerchant.data.RemoteRepository
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_TOPUP
import network.omisego.omgmerchant.utils.stringRes
import org.amshove.kluent.mock
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.Date

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23])
class ConfirmViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    val mockRemoteRepository: RemoteRepository = mock()
    val mockLocalRepository: LocalRepository = mock()
    private val viewModel: ConfirmViewModel by lazy { ConfirmViewModel(RuntimeEnvironment.application, mockLocalRepository, mockRemoteRepository) }
    private val mockArgs: ConfirmFragmentArgs by lazy { mock<ConfirmFragmentArgs>() }

    @Before
    fun setup() {
        viewModel.args = mockArgs
    }

    @Test
    fun `test amount text is displayed correctly`() {
        whenever(mockArgs.amount).thenReturn("100")
        whenever(mockArgs.token).thenReturn(mock())
        whenever(mockArgs.token.symbol).thenReturn("OMG")

        viewModel.amountText shouldEqualTo "100.00 OMG"
    }

    @Test
    fun `test transaction direction text is displayed correctly`() {
        whenever(mockArgs.transactionType).thenReturn(SCAN_RECEIVE)
        viewModel.transactionDirectionText shouldEqualTo stringRes(R.string.confirm_transaction_direction_from)

        whenever(mockArgs.transactionType).thenReturn(SCAN_TOPUP)
        viewModel.transactionDirectionText shouldEqualTo stringRes(R.string.confirm_transaction_direction_to)
    }

    @Test
    fun `test transaction type text is displayed correctly`() {
        whenever(mockArgs.transactionType).thenReturn(SCAN_RECEIVE)
        viewModel.transactionTypeText shouldEqualTo stringRes(R.string.confirm_transaction_type_receive)

        whenever(mockArgs.transactionType).thenReturn(SCAN_TOPUP)
        viewModel.transactionTypeText shouldEqualTo stringRes(R.string.confirm_transaction_type_top_up)
    }

    @Test
    fun `test get_user_wallet call remote repository to load wallet correctly`() {
        viewModel.getUserWallet("wallet_address")
        verify(mockRemoteRepository).loadWallet(WalletParams("wallet_address"), viewModel.liveWallet)
        verifyNoMoreInteractions(mockRemoteRepository)
    }

    @Test
    fun `test when yes button is clicked then the LiveData value should be changed`() {
        val mockView = mock<View>()
        viewModel.handleYesClick(mockView)
        viewModel.liveYesClick.value?.peekContent() shouldEqual mockView
    }

    @Test
    fun `test when no button is clicked then the LiveData value should be changed`() {
        val mockView = mock<View>()
        viewModel.handleNoClick(mockView)
        viewModel.liveNoClick.value?.peekContent() shouldEqual mockView
    }

    @Test
    fun `test the transaction_create_params is created with correct properties`() {
        whenever(mockLocalRepository.loadWallet()).thenReturn(mock())
        whenever(mockLocalRepository.loadWallet()?.address).thenReturn("to_address")
        whenever(mockArgs.transactionType).thenReturn(SCAN_RECEIVE)
        whenever(mockArgs.amount).thenReturn("100")
        whenever(mockArgs.token).thenReturn(mock())
        whenever(mockArgs.token.subunitToUnit).thenReturn(10.bd)
        whenever(mockArgs.token.id).thenReturn("tok_omg")

        /* Verify transaction create params for receive */
        val expectedTransactionCreateParamsReceive = TransactionCreateParams(
            "from_address",
            "to_address",
            amount = 1000.bd,
            tokenId = "tok_omg"
        )
        with(viewModel.provideTransactionCreateParams("from_address")) {
            fromAddress shouldEqual expectedTransactionCreateParamsReceive.fromAddress
            toAddress shouldEqual expectedTransactionCreateParamsReceive.toAddress
            amount shouldEqual expectedTransactionCreateParamsReceive.amount
            tokenId shouldEqual expectedTransactionCreateParamsReceive.tokenId
        }

        /* Verify transaction create params for topup */
        whenever(mockArgs.transactionType).thenReturn(SCAN_TOPUP)
        val expectedTransactionCreateParamsTopUp = TransactionCreateParams(
            "to_address",
            "from_address",
            amount = 1000.bd,
            tokenId = "tok_omg"
        )
        with(viewModel.provideTransactionCreateParams("from_address")) {
            fromAddress shouldEqual expectedTransactionCreateParamsTopUp.fromAddress
            toAddress shouldEqual expectedTransactionCreateParamsTopUp.toAddress
            amount shouldEqual expectedTransactionCreateParamsTopUp.amount
            tokenId shouldEqual expectedTransactionCreateParamsTopUp.tokenId
        }
    }

    @Test
    fun `test the feedback can be created with transaction`() {
        val mockTransaction: Transaction = mock()
        val mockTransactionSourceFrom: TransactionSource = mock()
        val mockTransactionSourceTo: TransactionSource = mock()
        val mockDate = mock<Date>()

        whenever(mockTransaction.createdAt).thenReturn(mockDate)
        whenever(mockTransaction.from).thenReturn(mockTransactionSourceFrom)
        whenever(mockTransaction.to).thenReturn(mockTransactionSourceTo)

        whenever(mockArgs.transactionType).thenReturn(SCAN_RECEIVE)
        viewModel.createFeedback(mockTransaction) shouldEqual Feedback(
            true,
            SCAN_RECEIVE,
            mockDate,
            mockTransactionSourceFrom,
            null
        )

        whenever(mockArgs.transactionType).thenReturn(SCAN_TOPUP)
        viewModel.createFeedback(mockTransaction) shouldEqual Feedback(
            true,
            SCAN_TOPUP,
            mockDate,
            mockTransactionSourceTo,
            null
        )
    }

    @Test
    fun `test the feedback can be created with wallet`() {
        val mockWallet: Wallet = mock()
        val mockUser: User = mock()
        val mockToken = mock<Token>()
        val mockAPIError: APIError = mock()

        viewModel.error = mockAPIError
        whenever(mockArgs.transactionType).thenReturn(SCAN_RECEIVE)
        whenever(mockArgs.amount).thenReturn("100")
        whenever(mockArgs.token).thenReturn(mockToken)
        whenever(mockArgs.token.subunitToUnit).thenReturn(100.bd)
        whenever(mockArgs.token.symbol).thenReturn("OMG")
        whenever(mockArgs.token.id).thenReturn("tok_omg")
        whenever(mockWallet.address).thenReturn("my_address")
        whenever(mockWallet.userId).thenReturn("user_01")
        whenever(mockWallet.user).thenReturn(mockUser)

        val expectedFeedback = Feedback(
            false,
            SCAN_RECEIVE,
            Date(),
            TransactionSource(
                "my_address",
                10000.bd,
                "tok_omg",
                mockToken,
                "user_01",
                mockUser,
                null,
                null
            ),
            mockAPIError
        )

        with(viewModel.createFeedback(mockWallet)) {
            transactionType shouldEqualTo expectedFeedback.transactionType
            error shouldEqual expectedFeedback.error
            source shouldEqual expectedFeedback.source
            success shouldEqualTo expectedFeedback.success
        }
    }

    @Test
    fun `test transfer should call RemoteRepository's transfer`() {
        val mockParams: TransactionCreateParams = mock()
        viewModel.transfer(mockParams)
        verify(mockRemoteRepository).transfer(mockParams, viewModel.liveTransaction)
    }
}
