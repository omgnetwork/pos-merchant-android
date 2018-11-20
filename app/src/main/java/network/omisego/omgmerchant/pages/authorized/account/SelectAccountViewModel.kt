package network.omisego.omgmerchant.pages.authorized.account

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.Account
import network.omisego.omgmerchant.custom.CustomStateViewHolderBinding
import network.omisego.omgmerchant.databinding.ViewholderAccountBinding
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.repository.LocalRepository
import network.omisego.omgmerchant.repository.RemoteRepository

class SelectAccountViewModel(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : ViewModel(), CustomStateViewHolderBinding<Account, ViewholderAccountBinding> {
    val liveAccountList: MutableLiveData<Event<APIResult>> by lazy { MutableLiveData<Event<APIResult>>() }
    val liveAccountSelect: MutableLiveData<Account> by lazy { MutableLiveData<Account>() }

    override fun bind(binding: ViewholderAccountBinding, data: Account) {
        binding.account = data
        binding.viewModel = this
        binding.ivAccountLogo.clipToOutline = true
    }

    fun handleAccountClick(account: Account) {
        localRepository.saveAccount(account)
        liveAccountSelect.value = account
    }

    fun loadAccounts() {
        remoteRepository.loadAccounts(liveAccountList)
    }
}
