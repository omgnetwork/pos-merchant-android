package network.omisego.omgmerchant.network

import co.omisego.omisego.OMGAPIAdmin
import co.omisego.omisego.model.AdminConfiguration
import co.omisego.omisego.network.ewallet.EWalletAdmin
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.coroutines.experimental.async
import network.omisego.omgmerchant.model.Credential
import network.omisego.omgmerchant.storage.Storage
import okhttp3.logging.HttpLoggingInterceptor

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object ClientProvider {
    private val credential: Credential
        get() = Storage.loadCredential()

    private lateinit var adminConfiguration: AdminConfiguration
    lateinit var client: OMGAPIAdmin
    val deferredClient = async {
        adminConfiguration = AdminConfiguration(
            "https://ewallet.demo.omisego.io/api/admin/",
            credential.userId,
            credential.authenticationToken
        )
        create()
    }

    fun init() {
        async { client = deferredClient.await() }
    }

    private fun create(): OMGAPIAdmin {
        return OMGAPIAdmin(
            EWalletAdmin.Builder {
                clientConfiguration = adminConfiguration
                debug = true
                debugOkHttpInterceptors = mutableListOf(
                    StethoInterceptor(),
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    }
                )
            }.build()
        )
    }
}
