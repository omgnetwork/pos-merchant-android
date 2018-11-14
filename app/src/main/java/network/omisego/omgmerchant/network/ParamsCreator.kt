package network.omisego.omgmerchant.network

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 14/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.model.params.AccountListParams
import co.omisego.omisego.model.params.AccountWalletListParams
import co.omisego.omisego.model.params.LoginParams
import co.omisego.omisego.model.params.TokenListParams
import co.omisego.omisego.model.params.TransactionConsumptionActionParams
import co.omisego.omisego.model.params.TransactionListParams
import co.omisego.omisego.model.params.TransactionRequestParams
import co.omisego.omisego.model.params.WalletParams
import co.omisego.omisego.model.params.admin.TransactionConsumptionParams
import co.omisego.omisego.model.params.admin.TransactionCreateParams
import network.omisego.omgmerchant.model.AmountFormat

class ParamsCreator {
    fun createTransactionRequestParams(formattedId: String): TransactionRequestParams {
        return TransactionRequestParams(formattedId)
    }

    fun createTransactionConsumptionActionParams(id: String): TransactionConsumptionActionParams {
        return TransactionConsumptionActionParams(id)
    }

    fun createLoginParams(email: String, password: String): LoginParams {
        return LoginParams(email, password)
    }

    fun createLoadWalletParams(address: String): WalletParams {
        return WalletParams(address)
    }

    fun createAccountWalletListParams(
        id: String,
        searchTerm: String? = null
    ): AccountWalletListParams {
        return AccountWalletListParams.create(id, searchTerm = searchTerm)
    }

    fun createLoadAccountParams(searchTerm: String? = null): AccountListParams {
        return AccountListParams.create(searchTerm = searchTerm)
    }

    fun createGetTransactionRequestParams(formattedId: String): TransactionRequestParams {
        return TransactionRequestParams(formattedId)
    }

    fun createLoadTransactionsParams(
        page: Int,
        perPage: Int = 20,
        searchTerm: String? = null
    ): TransactionListParams {
        return TransactionListParams.create(searchTerm = searchTerm)
    }

    fun createListTokensParams(perPage: Int = 30, searchTerm: String? = null): TokenListParams {
        return TokenListParams.create(perPage = perPage, searchTerm = searchTerm)
    }

    fun createTransactionCreateParams(
        fromAddress: String,
        toAddress: String,
        formattedAmount: AmountFormat.Unit,
        tokenId: String
    ): TransactionCreateParams {
        return TransactionCreateParams(
            fromAddress = fromAddress,
            toAddress = toAddress,
            amount = formattedAmount.toSubunit().amount,
            tokenId = tokenId
        )
    }

    fun createTransactionConsumptionParams(
        formattedId: String,
        formattedAmount: AmountFormat.Unit,
        tokenId: String,
        accountId: String? = null,
        exchangeAccountId: String? = null,
        exchangeWalletAddress: String? = null
    ): TransactionConsumptionParams {
        return TransactionConsumptionParams.create(
            formattedTransactionRequestId = formattedId,
            amount = formattedAmount.toSubunit().amount,
            tokenId = tokenId,
            accountId = accountId,
            exchangeAccountId = exchangeAccountId,
            exchangeWalletAddress = exchangeWalletAddress
        )
    }
}
