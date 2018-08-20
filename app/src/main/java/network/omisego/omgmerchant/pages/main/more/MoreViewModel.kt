package network.omisego.omgmerchant.pages.main.more

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.extensions.mutableLiveDataOf
import network.omisego.omgmerchant.model.SettingMenu
import network.omisego.omgmerchant.storage.Storage

class MoreViewModel(
    val app: Application
) : AndroidViewModel(app) {
    val menus: List<SettingMenu> = listOf(
        SettingMenu("\uE918", app.getString(R.string.more_account)),
        SettingMenu("\uE921", app.getString(R.string.more_transaction)),
        SettingMenu("\uE91F", app.getString(R.string.more_setting_and_help))
    )
    val liveSignOut: MutableLiveData<Boolean> by lazy { mutableLiveDataOf(false) }
    val liveMenu: MutableLiveData<SettingMenu> by lazy { mutableLiveDataOf<SettingMenu>() }

    fun signOut() {
        Storage.clearEverything()
        liveSignOut.value = true
    }

    fun handleClickMenu(menu: SettingMenu) {
        liveMenu.value = menu
    }
}
