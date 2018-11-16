package network.omisego.omgmerchant.pages.authorized

import network.omisego.omgmerchant.NavBottomNavigationDirections
import network.omisego.omgmerchant.model.Feedback

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

interface ConfirmNavDirectionCreator {
    fun createDestinationFeedback(feedback: Feedback): NavBottomNavigationDirections.ActionGlobalFeedbackFragment

    fun createDestinationLoading(): NavBottomNavigationDirections.ActionGlobalLoadingFragment
}