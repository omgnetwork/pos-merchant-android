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

class SelectAccountViewModel(
    private val selectAccountRepository: SelectAccountRepository
) : ViewModel() {
    private val errorResponse: MutableLiveData<APIError> by lazy { MutableLiveData<APIError>() }
    private val successResponse: MutableLiveData<List<Account>> by lazy { MutableLiveData<List<Account>>() }

    fun loadAccounts() {
        selectAccountRepository.loadAccounts(successResponse to errorResponse)
    }

    fun subscribeLoadAccounts(): Pair<MutableLiveData<List<Account>>, MutableLiveData<APIError>> {
        return successResponse to errorResponse
    }
}
