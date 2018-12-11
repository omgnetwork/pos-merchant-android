package network.omisego.omgmerchant.network

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.OMGAPIAdmin
import co.omisego.omisego.custom.retrofit2.executor.MainThreadExecutor
import co.omisego.omisego.model.AdminConfiguration
import co.omisego.omisego.network.ewallet.EWalletAdmin
import co.omisego.omisego.websocket.OMGSocketClient
import co.omisego.omisego.websocket.SocketClientContract
import com.facebook.stetho.okhttp3.StethoInterceptor
import network.omisego.omgmerchant.BuildConfig
import network.omisego.omgmerchant.model.Credential
import network.omisego.omgmerchant.storage.Storage
import okhttp3.HttpUrl
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.Executor

object ClientProvider {
    private val credential: Credential
        get() = Storage.loadCredential()

    private val adminConfiguration: AdminConfiguration by lazy {
        AdminConfiguration(
            BuildConfig.CLIENT_API_BASE_URL,
            credential.userId,
            credential.authenticationToken
        )
    }

    var client: OMGAPIAdmin
    lateinit var socketClient: SocketClientContract.Client

    init {
        client = create()
    }

    fun create(debugUrl: HttpUrl? = null, executor: Executor = MainThreadExecutor()): OMGAPIAdmin {
        return OMGAPIAdmin(
            EWalletAdmin.Builder {
                clientConfiguration = adminConfiguration
                debug = true
                callbackExecutor = executor
                this.debugUrl = debugUrl
                debugOkHttpInterceptors = mutableListOf(
                    StethoInterceptor(),
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    }
                )
            }.build()
        )
    }

    fun createSocketClient(credential: Credential): SocketClientContract.Client {
        return OMGSocketClient.Builder {
            clientConfiguration = adminConfiguration.copy(
                baseURL = BuildConfig.CLIENT_API_SOCKET_BASE_URL,
                authenticationToken = credential.authenticationToken,
                userId = credential.userId
            )
        }.build()
    }
}
