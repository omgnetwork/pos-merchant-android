package network.omisego.omgmerchant.calculator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 9/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

data class CalculatorState(
    var containOperator: Boolean,
    var clearBeforeAppend: Boolean,
    var recentButton: CalculatorButton
)
