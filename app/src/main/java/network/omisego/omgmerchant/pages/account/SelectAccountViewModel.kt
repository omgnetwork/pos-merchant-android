package network.omisego.omgmerchant.pages.account

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.Account
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.StateRecyclerAdapter
import network.omisego.omgmerchant.base.StateViewHolderBinding
import network.omisego.omgmerchant.databinding.ViewholderAccountBinding
import network.omisego.omgmerchant.extensions.mutableLiveDataOf
import network.omisego.omgmerchant.storage.Storage

class SelectAccountViewModel(
    private val selectAccountRepository: SelectAccountRepository
) : ViewModel(), StateViewHolderBinding<Account, ViewholderAccountBinding> {
    val liveState: MutableLiveData<StateRecyclerAdapter<Account>> by lazy {
        MutableLiveData<StateRecyclerAdapter<Account>>().apply {
            value = StateRecyclerAdapter.Loading(layout = R.layout.viewholder_account_loading)
        }
    }
    val liveAccountSelect: MutableLiveData<Account> by lazy { mutableLiveDataOf<Account>() }

    override fun bind(binding: ViewholderAccountBinding, data: Account) {
        binding.account = data
        binding.viewModel = this
    }

    fun handleAccountClick(account: Account) {
        Storage.saveAccount(account)
        liveAccountSelect.value = account
    }

    fun loadAccounts() = selectAccountRepository.loadAccounts()
}
