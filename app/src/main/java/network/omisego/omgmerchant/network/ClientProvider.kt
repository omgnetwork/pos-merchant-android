package network.omisego.omgmerchant.network

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.OMGAPIAdmin
import co.omisego.omisego.model.AdminConfiguration
import co.omisego.omisego.network.ewallet.EWalletAdmin
import co.omisego.omisego.websocket.OMGSocketClient
import co.omisego.omisego.websocket.SocketClientContract
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.coroutines.experimental.async
import network.omisego.omgmerchant.model.Credential
import network.omisego.omgmerchant.storage.Storage
import okhttp3.logging.HttpLoggingInterceptor

object ClientProvider {
    private val credential: Credential
        get() = Storage.loadCredential()

    private lateinit var adminConfiguration: AdminConfiguration
    lateinit var client: OMGAPIAdmin
    lateinit var socketClient: SocketClientContract.Client
    val deferredClient = async {
        adminConfiguration = AdminConfiguration(
            "https://coffeego.omisego.io/api/admin/",
            credential.userId,
            credential.authenticationToken
        )
        create()
    }

    fun init() {
        async {
            client = deferredClient.await()
            socketClient = createSocketClient()
        }
    }

    init {
        init()
    }

    private fun create(): OMGAPIAdmin {
        return OMGAPIAdmin(
            EWalletAdmin.Builder {
                clientConfiguration = adminConfiguration
                debug = true
                debugOkHttpInterceptors = mutableListOf(
                    StethoInterceptor(),
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            }.build()
        )
    }

    private fun createSocketClient(): SocketClientContract.Client {
        return OMGSocketClient.Builder {
            clientConfiguration = adminConfiguration.copy(baseURL = "wss://coffeego.omisego.io/api/admin/socket/")
        }.build()
    }
}
