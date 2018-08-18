package network.omisego.omgmerchant.pages.main

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 16/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import co.omisego.omisego.model.params.TokenListParams
import kotlinx.coroutines.experimental.async
import network.omisego.omgmerchant.extensions.subscribe
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.network.ClientProvider

class TokenRepository {
    fun listTokens(params: TokenListParams, liveAPIResult: MutableLiveData<APIResult>) {
        async {
            ClientProvider.deferredClient.await()
                .getTokens(params)
                .subscribe(liveAPIResult)
        }
    }
}
