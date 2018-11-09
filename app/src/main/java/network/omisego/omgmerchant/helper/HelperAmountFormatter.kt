package network.omisego.omgmerchant.helper

import co.omisego.omisego.model.Token
import network.omisego.omgmerchant.model.AmountFormat
import java.math.BigDecimal

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 8/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class HelperAmountFormatter {
    fun displayDecimal(amount: BigDecimal, subunit: BigDecimal): Int {
        return if (amount.scale() < subunit.precision()) {
            amount.scale()
        } else {
            subunit.precision()
        }
    }

    fun format(amountUnit: AmountFormat.Unit, token: Token): String {
        return String.format(
            "%s %s",
            amountUnit.display(),
            token.symbol
        )
    }
}