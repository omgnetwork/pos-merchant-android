package network.omisego.omgmerchant.model

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 8/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import java.math.BigDecimal

sealed class AmountFormat {
    abstract val amount: BigDecimal
    abstract val subunit: BigDecimal

    /**
     * Used when communicate between client and server
     *
     * @param amount The subunit amount
     * @param subunit the subunitToUnit of the token
     */
    data class Subunit(
        override val amount: BigDecimal,
        override val subunit: BigDecimal
    ) : AmountFormat() {
        fun toUnit() = Unit(amount.divide(subunit), subunit)
    }

    /**
     * Used when show to user
     * @param amount The unit amount
     * @param subunit The subunitToUnit of the token
     */
    data class Unit(
        override val amount: BigDecimal,
        override val subunit: BigDecimal
    ) : AmountFormat() {
        fun toSubunit() = Subunit(amount.multiply(subunit).setScale(0), subunit)
        fun display(): String {
            val decimal = if (amount.scale() < subunit.precision()) {
                amount.scale()
            } else {
                subunit.precision()
            }

            return String.format("%,.${decimal}f", amount)
        }
    }
}
