package network.omisego.omgmerchant.storage

import android.content.Context
import android.content.SharedPreferences
import co.omisego.omisego.model.Account
import co.omisego.omisego.model.User
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.security.OMGKeyManager
import co.omisego.omisego.utils.GsonProvider
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
        sharePref[StorageKey.KEY_AUTHENTICATION_TOKEN encryptWith keyManager] = credential.authenticationToken encryptWith keyManager
        sharePref[StorageKey.KEY_USER_ID encryptWith keyManager] = credential.userId encryptWith keyManager
    }

    fun loadCredential(): Credential {
        val userId = sharePref[StorageKey.KEY_USER_ID encryptWith keyManager]
        val authenticationToken = sharePref[StorageKey.KEY_AUTHENTICATION_TOKEN encryptWith keyManager]
        if (userId.isNullOrEmpty()) {
            return Credential("", "")
        }
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
