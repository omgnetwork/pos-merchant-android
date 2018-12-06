package network.omisego.omgmerchant.pages.authorized

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import network.omisego.omgmerchant.NavBottomNavigationDirections
import network.omisego.omgmerchant.model.Feedback

interface LoadingNavDirectionCreator {
    fun createDestinationFeedback(feedback: Feedback): NavBottomNavigationDirections.ActionGlobalFeedbackFragment
}
