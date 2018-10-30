package network.omisego.omgmerchant.pages.authorized.main.more.account

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.Account
import co.omisego.omisego.model.params.AccountWalletListParams
import network.omisego.omgmerchant.base.StateViewHolderBinding
import network.omisego.omgmerchant.databinding.ViewholderSettingAccountBinding
import network.omisego.omgmerchant.extensions.mutableLiveDataOf
import network.omisego.omgmerchant.pages.authorized.main.WalletRepository
import network.omisego.omgmerchant.storage.Storage

class SettingAccountViewModel(
    private val settingAccountRepository: SettingAccountRepository,
    private val walletRepository: WalletRepository
) : ViewModel(), StateViewHolderBinding<Account, ViewholderSettingAccountBinding> {
    val liveAccountSelect: MutableLiveData<Account> by lazy {
        mutableLiveDataOf(settingAccountRepository.getCurrentAccount())
    }

    override fun bind(binding: ViewholderSettingAccountBinding, data: Account) {
        binding.account = data
        binding.viewModel = this
        binding.ivAccountLogo.clipToOutline = true
    }

    fun saveAccount(): Account {
        Storage.saveAccount(liveAccountSelect.value!!)
        return liveAccountSelect.value!!
    }

    fun loadAccount(): Account {
        return settingAccountRepository.getCurrentAccount()!!
    }

    fun handleAccountClick(account: Account) {
        walletRepository.loadWalletAndSave(
            AccountWalletListParams.create(id = account.id, searchTerm = null)
        )
        liveAccountSelect.value = account
    }

    fun loadAccounts() = settingAccountRepository.loadAccounts()
}