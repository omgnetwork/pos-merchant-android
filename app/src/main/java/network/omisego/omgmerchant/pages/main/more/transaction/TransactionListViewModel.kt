package network.omisego.omgmerchant.pages.main.more.transaction

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import co.omisego.omisego.model.Account
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.model.pagination.Paginable
import co.omisego.omisego.model.transaction.Transaction
import co.omisego.omisego.model.transaction.TransactionSource
import co.omisego.omisego.model.transaction.list.TransactionListParams
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.StateViewHolderBinding
import network.omisego.omgmerchant.databinding.ViewholderTransactionBinding
import network.omisego.omgmerchant.extensions.mutableLiveDataOf
import network.omisego.omgmerchant.model.APIResult

class TransactionListViewModel(
    private val app: Application,
    private val repository: TransactionListRepository
) : AndroidViewModel(app), StateViewHolderBinding<Transaction, ViewholderTransactionBinding> {
    val liveTransactionFailedDescription: MutableLiveData<String> by lazy { mutableLiveDataOf("") }
    val account: Account
        get() = repository.getAccount()!!
    val wallet: Wallet
        get() = repository.getWallet()!!
    val TransactionSource.username: String
        get() = "${this.user?.metadata?.get("first_name")} ${this.user?.metadata?.get("last_name")}"
    val Transaction.isTopup: Boolean
        get() = this.from.accountId != null

    override fun bind(binding: ViewholderTransactionBinding, data: Transaction) {
        binding.transaction = data
        binding.viewModel = this
    }

    fun formatUsername(transaction: Transaction): String {
        return if (transaction.isTopup) {
            app.getString(
                R.string.transaction_list_info_name_id,
                transaction.to.username,
                "${transaction.to.userId?.take(8)}...${transaction.to.userId?.takeLast(3)}"
            )
        } else {
            app.getString(
                R.string.transaction_list_info_name_id,
                transaction.from.username,
                "${transaction.from.userId?.take(8)}...${transaction.from.userId?.takeLast(3)}"
            )
        }
    }

    fun formatDescription(transaction: Transaction): String {
        return when (transaction.status) {
            Paginable.Transaction.TransactionStatus.CONFIRMED -> {
                if (transaction.to.accountId != null) {
                    app.getString(R.string.receive_title)
                } else {
                    app.getString(R.string.topup_title)
                }
            }
            else -> {
                transaction.status.value.capitalize()
            }
        }
    }

    fun formatAmount(transaction: Transaction): String {
        return app.getString(
            R.string.transaction_list_info_amount,
            transaction.from.amount.divide(transaction.from.token.subunitToUnit),
            transaction.from.token.symbol
        )
    }

    fun formatDate(transaction: Transaction): String {
        return app.getString(R.string.transaction_list_info_date_time, transaction.createdAt)
    }

    fun giveTransactionStatusDescription(transaction: Transaction) {
        transaction.error?.let {
            liveTransactionFailedDescription.value = transaction.error?.description
        }
        if (transaction.error == null) {
            liveTransactionFailedDescription.value = if (transaction.isTopup) {
                app.getString(
                    R.string.transaction_list_topup_info,
                    transaction.to.amount.divide(transaction.to.token.subunitToUnit),
                    transaction.to.token.symbol,
                    transaction.to.userId
                )
            } else {
                app.getString(
                    R.string.transaction_list_receive_info,
                    transaction.from.amount.divide(transaction.from.token.subunitToUnit),
                    transaction.from.token.symbol,
                    transaction.from.userId
                )
            }
        }
    }

    fun getTransaction(page: Int): LiveData<APIResult> {
        val params = TransactionListParams.create(
            page = page,
            perPage = 20,
            searchTerm = wallet.address
        )
        return repository.getTransactions(params)
    }
}
