package network.omisego.omgmerchant.pages.authorized

import co.omisego.omisego.model.Token
import network.omisego.omgmerchant.NavBottomNavigationDirections
import network.omisego.omgmerchant.NavBottomNavigationDirections.ActionGlobalScanFragment
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.authorized.main.CalculatorMode
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_TOPUP

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class NavDirectionCreator : MainNavDirectionCreator, ConfirmNavDirectionCreator {
    override fun createDestinationQRScan(
        mode: CalculatorMode,
        amount: String,
        token: Token
    ): ActionGlobalScanFragment {
        return when (mode) {
            CalculatorMode.RECEIVE -> {
                ActionGlobalScanFragment(token)
                    .setAmount(amount)
                    .setTransactionType(SCAN_RECEIVE)
            }
            CalculatorMode.TOPUP -> {
                ActionGlobalScanFragment(token)
                    .setAmount(amount)
                    .setTransactionType(SCAN_TOPUP)
            }
        }
    }

    override fun createDestinationSelectAccount(): NavBottomNavigationDirections.ActionGlobalSelectAccountFragment {
        return NavBottomNavigationDirections.actionGlobalSelectAccountFragment()
    }

    override fun createDestinationSplash(): NavBottomNavigationDirections.ActionGlobalSplashFragment {
        return NavBottomNavigationDirections.actionGlobalSplashFragment()
    }

    override fun createDestinationFeedback(feedback: Feedback): NavBottomNavigationDirections.ActionGlobalFeedbackFragment {
        return NavBottomNavigationDirections.ActionGlobalFeedbackFragment(feedback)
    }

    override fun createDestinationLoading() : NavBottomNavigationDirections.ActionGlobalLoadingFragment {
        return NavBottomNavigationDirections.ActionGlobalLoadingFragment()
    }
}