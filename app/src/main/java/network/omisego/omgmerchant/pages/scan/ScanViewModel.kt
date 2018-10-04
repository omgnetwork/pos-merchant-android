package network.omisego.omgmerchant.pages.scan

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 16/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import network.omisego.omgmerchant.R

class ScanViewModel(
    private val app: Application
) : AndroidViewModel(app) {
    lateinit var args: ScanFragmentArgs

    val amountText: String
        get() = app.getString(R.string.scan_amount, args.amount.toBigDecimal(), args.token.symbol)

    val title: String
        get() {
            return if (args.transactionType == SCAN_RECEIVE) {
                app.getString(R.string.scan_title_payment)
            } else {
                app.getString(R.string.scan_title_topup)
            }
        }
}
