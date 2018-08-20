package network.omisego.omgmerchant.storage

import android.content.Context
import android.content.SharedPreferences
import co.omisego.omisego.model.Account
import co.omisego.omisego.model.User
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.security.OMGKeyManager
import co.omisego.omisego.utils.GsonProvider
import kotlinx.coroutines.experimental.async
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.extensions.decryptWith
import network.omisego.omgmerchant.extensions.encryptWith
import network.omisego.omgmerchant.extensions.get
import network.omisego.omgmerchant.extensions.set
import network.omisego.omgmerchant.model.Credential
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.utils.Contextor.context

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object Storage {
    private val gson by lazy { GsonProvider.create() }
    private val sharePref: SharedPreferences by lazy {
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    }

    private val keyManager: OMGKeyManager by lazy {
        OMGKeyManager.Builder {
            keyAlias = "omg"
            iv = "omg:12345678"
        }.build(context)
    }

    fun saveCredential(credential: Credential) {
        async {
            sharePref[StorageKey.KEY_AUTHENTICATION_TOKEN] = credential.authenticationToken encryptWith keyManager
            sharePref[StorageKey.KEY_USER_ID] = credential.userId encryptWith keyManager
        }
    }

    fun clearEverything() {
        sharePref.edit()
            .remove(StorageKey.KEY_USER_ID)
            .remove(StorageKey.KEY_AUTHENTICATION_TOKEN)
            .remove(StorageKey.KEY_ACCOUNT)
            .remove(StorageKey.KEY_WALLET)
            .remove(StorageKey.KEY_USER)
            .remove(StorageKey.KEY_FEEDBACK)
            .apply()
    }

    fun hasCredential() =
        sharePref.contains(StorageKey.KEY_AUTHENTICATION_TOKEN) && sharePref.contains(StorageKey.KEY_USER_ID)

    fun loadCredential(): Credential {
        if (!hasCredential()) {
            return Credential("", "")
        }
        val userId = sharePref[StorageKey.KEY_USER_ID]!!
        val authenticationToken = sharePref[StorageKey.KEY_AUTHENTICATION_TOKEN]!!
        return Credential(
            userId decryptWith keyManager,
            authenticationToken decryptWith keyManager
        )
    }

    fun saveAccount(account: Account) {
        sharePref[StorageKey.KEY_ACCOUNT] = gson.toJson(account)
    }

    fun loadAccount(): Account? {
        if (sharePref[StorageKey.KEY_ACCOUNT].isNullOrEmpty()) return null
        return gson.fromJson<Account>(sharePref[StorageKey.KEY_ACCOUNT], Account::class.java)
    }

    fun saveWallet(wallet: Wallet) {
        sharePref[StorageKey.KEY_WALLET] = gson.toJson(wallet)
    }

    fun loadWallet(): Wallet? {
        if (sharePref[StorageKey.KEY_WALLET].isNullOrEmpty()) return null
        return gson.fromJson<Wallet>(sharePref[StorageKey.KEY_WALLET], Wallet::class.java)
    }

    fun saveUser(user: User) {
        sharePref[StorageKey.KEY_USER] = gson.toJson(user)
    }

    fun loadUser(): User? {
        if (sharePref[StorageKey.KEY_USER].isNullOrEmpty()) return null
        return gson.fromJson<User>(sharePref[StorageKey.KEY_USER], User::class.java)
    }

    fun saveFeedback(feedback: Feedback) {
        sharePref[StorageKey.KEY_FEEDBACK] = gson.toJson(feedback)
    }

    fun loadFeedback(): Feedback? {
        if (sharePref[StorageKey.KEY_FEEDBACK].isNullOrEmpty()) return null
        return gson.fromJson<Feedback>(sharePref[StorageKey.KEY_FEEDBACK], Feedback::class.java)
    }

    fun deleteFeedback() {
        sharePref[StorageKey.KEY_FEEDBACK] = ""
    }
}
