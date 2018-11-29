package network.omisego.omgmerchant.custom

import androidx.databinding.ViewDataBinding

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

interface CustomStateViewHolderBinding<T, R : ViewDataBinding> {
    fun bind(binding: R, data: T)
}
