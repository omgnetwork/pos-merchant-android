package network.omisego.omgmerchant.pages.authorized

import co.omisego.omisego.model.Token
import network.omisego.omgmerchant.NavBottomNavigationDirections
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.authorized.main.CalculatorMode

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

interface MainNavDirectionCreator {
    fun createDestinationQRScan(
        mode: CalculatorMode,
        amount: String,
        token: Token
    ): NavBottomNavigationDirections.ActionGlobalScanFragment

    fun createDestinationSelectAccount(): NavBottomNavigationDirections.ActionGlobalSelectAccountFragment

    fun createDestinationSplash(): NavBottomNavigationDirections.ActionGlobalSplashFragment

    fun createDestinationFeedback(feedback: Feedback): NavBottomNavigationDirections.ActionGlobalFeedbackFragment
}