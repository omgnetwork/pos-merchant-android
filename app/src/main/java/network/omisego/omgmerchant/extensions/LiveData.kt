package network.omisego.omgmerchant.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

fun <T> ViewModel.mutableLiveDataOf(initial: T? = null): MutableLiveData<T> {
    return MutableLiveData<T>().apply { this.value = initial }
}
