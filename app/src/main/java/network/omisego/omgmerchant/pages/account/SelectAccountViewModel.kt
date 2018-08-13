package network.omisego.omgmerchant.pages.account

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Account
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.StateRecyclerAdapter
import network.omisego.omgmerchant.base.StateViewHolderBinding
import network.omisego.omgmerchant.databinding.ViewholderAccountBinding

class SelectAccountViewModel(
    private val selectAccountRepository: SelectAccountRepository
) : ViewModel(), StateViewHolderBinding<Account, ViewholderAccountBinding> {
    private val errorResponse: MutableLiveData<APIError> by lazy { MutableLiveData<APIError>() }
    private val successResponse: MutableLiveData<List<Account>> by lazy { MutableLiveData<List<Account>>() }
    val liveState: MutableLiveData<StateRecyclerAdapter<Account>> by lazy {
        MutableLiveData<StateRecyclerAdapter<Account>>().apply {
            value = StateRecyclerAdapter.Loading(layout = R.layout.viewholder_account_loading)
        }
    }

    override fun bind(binding: ViewholderAccountBinding, data: Account) {
        binding.account = data
    }

    fun changeState(itemList: List<Account>) {
        liveState.value = StateRecyclerAdapter.Show(
            itemList,
            R.layout.viewholder_account,
            this
        )
    }

    fun loadAccounts() {
        selectAccountRepository.loadAccounts(successResponse to errorResponse)
    }

    fun subscribeLoadAccounts(): Pair<MutableLiveData<List<Account>>, MutableLiveData<APIError>> {
        return successResponse to errorResponse
    }
}
