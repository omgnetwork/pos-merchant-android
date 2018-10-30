package network.omisego.omgmerchant.pages.authorized.main.more.account

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 29/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.Account

class SaveAccountViewModel(
    private val repository: SettingAccountRepository
) : ViewModel() {
    val liveSaveAccount: MutableLiveData<Account> = MutableLiveData()
}
