package network.omisego.omgmerchant.data

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Account
import co.omisego.omisego.model.User
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.model.transaction.Transaction
import co.omisego.omisego.model.transaction.TransactionSource
import network.omisego.omgmerchant.model.Credential
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.storage.Storage
import java.util.Date

class LocalRepository() {

    /* Feedback */

    fun loadFeedback() = Storage.loadFeedback()

    fun saveFeedback(transactionType: String, transaction: Transaction) {
        val feedback =
            if (transactionType.equals(SCAN_RECEIVE, true)) Feedback(
                true,
                transactionType,
                transaction.createdAt,
                transaction.from
            ) else {
                Feedback(
                    true,
                    transactionType,
                    transaction.createdAt,
                    transaction.to
                )
            }
        Storage.saveFeedback(feedback)
    }

    fun saveFeedback(transactionType: String, source: TransactionSource, error: APIError? = null) {
        val feedback =
            if (transactionType.equals(SCAN_RECEIVE, true)) Feedback(
                false,
                transactionType,
                Date(),
                source,
                error
            ) else {
                Feedback(
                    false,
                    transactionType,
                    Date(),
                    source,
                    error
                )
            }
        Storage.saveFeedback(feedback)
    }

    fun deleteFeedback() {
        Storage.deleteFeedback()
    }

    /* Wallet */

    fun loadWallet(): Wallet? {
        return Storage.loadWallet()
    }

    /* Account */

    fun getAccount(): Account? = Storage.loadAccount()

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
