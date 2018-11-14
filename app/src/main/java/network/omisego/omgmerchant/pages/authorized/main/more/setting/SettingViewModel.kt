package network.omisego.omgmerchant.pages.authorized.main.more.setting

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.data.LocalRepository
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.SettingMenu

class SettingViewModel(
    val app: Application,
    val localRepository: LocalRepository
) : AndroidViewModel(app) {
    val menus: List<SettingMenu> = listOf(
        SettingMenu("\uE918", app.getString(R.string.more_account)),
        SettingMenu("\uE921", app.getString(R.string.more_transaction)),
        SettingMenu("\uE91F", app.getString(R.string.more_setting_and_help))
    )
    val liveSignOut: MutableLiveData<Event<Boolean>> by lazy { MutableLiveData<Event<Boolean>>() }
    private val liveMenu: MutableLiveData<Event<SettingMenu>> by lazy { MutableLiveData<Event<SettingMenu>>() }

    fun signOut() {
        localRepository.clearSession()
        liveSignOut.value = Event(true)
    }

    fun getLiveMenu(): LiveData<Event<SettingMenu>> = liveMenu

    fun setLiveMenu(menu: SettingMenu?) {
        liveMenu.value = Event(menu!!)
    }
}
