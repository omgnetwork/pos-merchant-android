package network.omisego.omgmerchant.network

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.extension.bd
import co.omisego.omisego.model.params.AccountWalletListParams
import co.omisego.omisego.model.params.LoginParams
import co.omisego.omisego.model.params.TokenListParams
import co.omisego.omisego.model.params.TransactionConsumptionActionParams
import co.omisego.omisego.model.params.TransactionListParams
import co.omisego.omisego.model.params.TransactionRequestParams
import co.omisego.omisego.model.params.WalletParams
import network.omisego.omgmerchant.model.AmountFormat
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldStartWith
import org.junit.Test

class ParamsCreatorTest {
    private val paramsCreator: ParamsCreator by lazy {
        ParamsCreator()
    }

    @Test
    fun `test create TransactionRequestParams`() {
        paramsCreator.createTransactionRequestParams("id") shouldEqual TransactionRequestParams("id")
    }

    @Test
    fun `test create TransactionConsumptionActionParams`() {
        paramsCreator.createTransactionConsumptionActionParams("id") shouldEqual TransactionConsumptionActionParams("id")
    }

    @Test
    fun `test create LoginParams`() {
        paramsCreator.createLoginParams("email", "password") shouldEqual LoginParams("email", "password")
    }

    @Test
    fun `test create LoadWalletParams`() {
        paramsCreator.createLoadWalletParams("address") shouldEqual WalletParams("address")
    }

    @Test
    fun `test create AccountWalletListParams`() {
        paramsCreator.createAccountWalletListParams("id", null) shouldEqual
            AccountWalletListParams.create("id", searchTerm = null)
    }

    @Test
    fun `test create LoadTransactionParams`() {
        paramsCreator.createLoadTransactionsParams(10, 20, null) shouldEqual TransactionListParams.create(
            page = 10,
            perPage = 20,
            searchTerm = null
        )
    }

    @Test
    fun `test create ListTokensParams`() {
        paramsCreator.createListTokensParams(30, null) shouldEqual TokenListParams.create(perPage = 30, searchTerm = null)
    }

    @Test
    fun `test create TransactionCreateParams`() {
        with(paramsCreator.createTransactionCreateParams(
            "from_address",
            "to_address",
            AmountFormat.Unit(10.bd, 100.bd),
            "tok_omg"
        )) {
            fromAddress shouldEqual "from_address"
            toAddress shouldEqual "to_address"
            amount shouldEqual 1000.bd
            tokenId shouldEqual "tok_omg"
        }
    }

    @Test
    fun `test create TransactionConsumptionParams`() {
        with(paramsCreator.createTransactionConsumptionParams(
            "formatted_id",
            AmountFormat.Unit(10.bd, 100.bd),
            "tok_omg",
            null,
            null,
            null
        )) {
            amount shouldEqual 1000.bd
            tokenId shouldEqual "tok_omg"
            idempotencyToken shouldStartWith "formatted_id"
            accountId shouldEqual null
            exchangeAccountId shouldEqual null
            exchangeWalletAddress shouldEqual null
        }
    }
}
