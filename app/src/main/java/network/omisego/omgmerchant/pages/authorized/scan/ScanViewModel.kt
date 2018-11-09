package network.omisego.omgmerchant.pages.authorized.scan

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 16/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.helper.HelperAmountFormatter
import network.omisego.omgmerchant.model.AmountFormat

class ScanViewModel(
    private val app: Application,
    private val amountFormatter: HelperAmountFormatter
) : AndroidViewModel(app) {
    lateinit var args: ScanFragmentArgs

    val amountText: String
        get() {
            return amountFormatter.format(
                AmountFormat.Unit(args.amount.toBigDecimal(), args.token.subunitToUnit),
                args.token
            )
        }

    val title: String
        get() {
            return if (args.transactionType == SCAN_RECEIVE) {
                app.getString(R.string.scan_title_payment)
            } else {
                app.getString(R.string.scan_title_topup)
            }
        }
}
