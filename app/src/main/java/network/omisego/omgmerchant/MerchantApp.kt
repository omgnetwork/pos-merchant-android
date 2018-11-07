package network.omisego.omgmerchant

import android.app.Application
import com.facebook.stetho.Stetho
import network.omisego.omgmerchant.utils.Contextor

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class MerchantApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Contextor.context = applicationContext
        Stetho.initializeWithDefaults(this)
    }
}
