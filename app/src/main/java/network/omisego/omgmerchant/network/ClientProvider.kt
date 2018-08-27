package network.omisego.omgmerchant.network

import co.omisego.omisego.OMGAPIAdmin
import co.omisego.omisego.model.AdminConfiguration
import co.omisego.omisego.network.ewallet.EWalletAdmin
import network.omisego.omgmerchant.model.Credential
import network.omisego.omgmerchant.storage.Storage

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object ClientProvider {
    private val credential: Credential
        get() = Storage.loadCredential()

    private var adminConfiguration = AdminConfiguration(
        "https://ewallet.demo.omisego.io/api/admin/",
        credential.userId,
        credential.authenticationToken
    )
    val client: OMGAPIAdmin by lazy { create() }

    private fun create(): OMGAPIAdmin {
        return OMGAPIAdmin(
            EWalletAdmin.Builder {
                clientConfiguration = adminConfiguration
                debug = true
            }.build()
        )
    }
}
