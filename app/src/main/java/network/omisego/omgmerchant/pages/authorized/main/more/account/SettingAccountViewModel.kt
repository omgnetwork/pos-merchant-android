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
import network.omisego.omgmerchant.custom.CustomStateViewHolderBinding
import network.omisego.omgmerchant.databinding.ViewholderSettingAccountBinding
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository

class SettingAccountViewModel(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : ViewModel(), CustomStateViewHolderBinding<Account, ViewholderSettingAccountBinding> {
    val liveAccountList: MutableLiveData<Event<APIResult>> by lazy { MutableLiveData<Event<APIResult>>() }
    val liveAccountSelect: MutableLiveData<Account> by lazy {
        MutableLiveData<Account>().apply {
            this.value = localRepository.loadAccount()!!
        }
    }

    override fun bind(binding: ViewholderSettingAccountBinding, data: Account) {
        binding.account = data
        binding.viewModel = this
        binding.ivAccountLogo.clipToOutline = true
    }

    fun saveAccount(): Account {
        localRepository.saveAccount(liveAccountSelect.value!!)
        return liveAccountSelect.value!!
    }

    fun loadSelectedAccount(): Account {
        return localRepository.loadAccount()!!
    }

    fun handleAccountClick(account: Account) {
        remoteRepository.loadWalletAndSave(
            AccountWalletListParams.create(id = account.id, searchTerm = null)
        )
        liveAccountSelect.value = account
    }

    fun loadAccounts() = remoteRepository.loadAccounts(liveAccountList)
}