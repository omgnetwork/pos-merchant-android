package network.omisego.omgmerchant.pages.main.more.settinghelp

import network.omisego.omgmerchant.storage.Storage

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class SettingHelpRepository {

    fun deleteFingerprintCredential() {
        Storage.deleteFingerprintCredential()
    }

    fun saveFingerprintOption(checked: Boolean) {
        Storage.saveFingerprintOption(checked)
    }

    fun loadFingerprintOption(): Boolean = Storage.loadFingerprintOption()
}
