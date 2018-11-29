package network.omisego.omgmerchant.pages.authorized.main.more.setting

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import network.omisego.omgmerchant.BuildConfig
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.model.SettingMenu
import network.omisego.omgmerchant.repository.LocalRepository

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
    val liveVersion: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val liveEndpoint: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private val liveMenu: MutableLiveData<Event<SettingMenu>> by lazy { MutableLiveData<Event<SettingMenu>>() }

    fun signOut() {
        localRepository.clearSession()
        liveSignOut.value = Event(true)
    }

    fun getLiveMenu(): LiveData<Event<SettingMenu>> = liveMenu

    fun setLiveMenu(menu: SettingMenu?) {
        liveMenu.value = Event(menu!!)
    }

    init {
        liveVersion.value = BuildConfig.VERSION_NAME
        liveEndpoint.value = BuildConfig.CLIENT_API_BASE_URL
    }
}
