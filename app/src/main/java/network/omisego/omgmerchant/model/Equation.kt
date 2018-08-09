package network.omisego.omgmerchant.model

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 9/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import network.omisego.omgmerchant.calculator.CalculatorButton
import java.math.BigDecimal

data class Equation(
    val left: BigDecimal,
    val operator: CalculatorButton,
    val right: BigDecimal
)
