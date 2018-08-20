package network.omisego.omgmerchant.pages.main.more.account

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.Account
import network.omisego.omgmerchant.base.StateRecyclerAdapter
import network.omisego.omgmerchant.base.StateViewHolderBinding
import network.omisego.omgmerchant.databinding.ViewholderSettingAccountBinding
import network.omisego.omgmerchant.extensions.mutableLiveDataOf
import network.omisego.omgmerchant.storage.Storage

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class SettingAccountViewModel(
    private val settingAccountRepository: SettingAccountRepository
) : ViewModel(), StateViewHolderBinding<Account, ViewholderSettingAccountBinding> {
    val liveState: MutableLiveData<StateRecyclerAdapter<Account>> by lazy {
        mutableLiveDataOf<StateRecyclerAdapter<Account>>()
    }
    val liveAccountSelect: MutableLiveData<Account> by lazy {
        mutableLiveDataOf(settingAccountRepository.getCurrentAccount())
    }

    override fun bind(binding: ViewholderSettingAccountBinding, data: Account) {
        binding.account = data
        binding.viewModel = this
    }

    fun handleAccountClick(account: Account) {
        Storage.saveAccount(account)
        liveAccountSelect.value = account
    }

    fun loadAccounts() = settingAccountRepository.loadAccounts()
}