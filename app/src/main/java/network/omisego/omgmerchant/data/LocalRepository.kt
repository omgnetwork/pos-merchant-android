package network.omisego.omgmerchant.data

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.model.Account
import co.omisego.omisego.model.User
import co.omisego.omisego.model.Wallet
import network.omisego.omgmerchant.model.Credential
import network.omisego.omgmerchant.storage.Storage

class LocalRepository() {

    /* Feedback */

    fun loadFeedback() = Storage.loadFeedback()

    fun deleteFeedback() {
        Storage.deleteFeedback()
    }

    /* Wallet */

    fun loadWallet(): Wallet? {
        return Storage.loadWallet()
    }

    /* Account */

    fun loadAccount(): Account? = Storage.loadAccount()

    fun saveAccount(account: Account) = Storage.saveAccount(account)

    /* Credential */

    fun hasCredential() = Storage.hasCredential()

    fun loadCredential() = Storage.loadCredential()

    fun saveCredential(credential: Credential) = Storage.saveCredential(credential)

    /* User */

    fun saveUser(user: User) = Storage.saveUser(user)

    /* Fingerprint */

    fun loadFingerprintOption(): Boolean = Storage.loadFingerprintOption()

    fun loadUserEmail() = Storage.loadUserEmail()

    fun loadFingerprintCredential() = Storage.loadFingerprintCredential()

    fun saveFingerprintOption(checked: Boolean) {
        Storage.saveFingerprintOption(checked)
    }

    fun saveUserEmail(email: String) = Storage.saveUserEmail(email)

    fun deleteFingerprintCredential() {
        Storage.deleteFingerprintCredential()
    }
}
