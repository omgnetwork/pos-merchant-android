package network.omisego.omgmerchant.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 1/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.livedata.EventObserver

fun <T> LifecycleOwner.observeFor(liveData: LiveData<T>, perform: (T) -> Unit) {
    liveData.observe(this, Observer {
        it ?: return@Observer
        perform(it)
    })
}

fun <T> LifecycleOwner.observeEventFor(liveData: LiveData<Event<T>>, perform: (T) -> Unit) {
    liveData.observe(this, EventObserver {
        perform(it)
    })
}
