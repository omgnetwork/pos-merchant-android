package network.omisego.omgmerchant.pages.authorized.main.more.transaction

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.model.Account
import co.omisego.omisego.model.Transaction
import co.omisego.omisego.model.Wallet
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.custom.CustomStateViewHolderBinding
import network.omisego.omgmerchant.databinding.ViewholderTransactionBinding
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.AmountFormat
import network.omisego.omgmerchant.network.ParamsCreator
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository

class TransactionListViewModel(
    private val app: Application,
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val transformer: TransactionListTransformer,
    private val paramsCreator: ParamsCreator = ParamsCreator()
) : AndroidViewModel(app), CustomStateViewHolderBinding<Transaction, ViewholderTransactionBinding> {

    /* Live data */
    val liveTransactionFailedDescription: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val liveTransactionListAPIResult: MutableLiveData<Event<APIResult>> by lazy { MutableLiveData<Event<APIResult>>() }

    /* get data from repository */
    val account: Account
        get() = localRepository.loadAccount()!!
    val wallet: Wallet
        get() = localRepository.loadWallet()!!

    /* helper function */
    val Transaction.isTopup: Boolean
        get() = this.from.accountId != null

    override fun bind(binding: ViewholderTransactionBinding, data: Transaction) {
        binding.transaction = data
        binding.viewModel = this
        binding.transformer = transformer
    }

    fun giveTransactionStatusDescription(transaction: Transaction) {
        transaction.error?.let {
            liveTransactionFailedDescription.value = transaction.error?.description
        }
        if (transaction.error == null) {
            liveTransactionFailedDescription.value = if (transaction.isTopup) {
                val subunit = AmountFormat.Subunit(transaction.to.amount, transaction.to.token.subunitToUnit)
                app.getString(
                    R.string.transaction_list_topup_info,
                    subunit.toUnit().display(),
                    transaction.to.token.symbol,
                    transaction.to.userId
                )
            } else {
                val subunit = AmountFormat.Subunit(transaction.from.amount, transaction.from.token.subunitToUnit)
                app.getString(
                    R.string.transaction_list_receive_info,
                    subunit.toUnit().display(),
                    transaction.from.token.symbol,
                    transaction.from.userId
                )
            }
        }
    }

    fun loadTransactionOnPage(page: Int) {
        val params = paramsCreator.createLoadTransactionsParams(
            page = page,
            perPage = 20,
            searchTerm = wallet.address
        )
        remoteRepository.loadTransactions(params, liveTransactionListAPIResult)
    }
}
