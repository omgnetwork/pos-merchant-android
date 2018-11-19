package network.omisego.omgmerchant.pages.authorized

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.model.Token
import network.omisego.omgmerchant.NavBottomNavigationDirections
import network.omisego.omgmerchant.model.Feedback
import network.omisego.omgmerchant.pages.authorized.main.CalculatorMode
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import org.amshove.kluent.mock
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23])
class NavDirectionCreatorTest {

    private val directionCreator: NavDirectionCreator by lazy {
        NavDirectionCreator()
    }

    @Test
    fun `test create destination QRScan`() {
        val mockToken: Token = mock()

        val destination = directionCreator.createDestinationQRScan(CalculatorMode.RECEIVE, "1000", mockToken)
        val args = destination.arguments

        args["token"] shouldEqual mockToken
        args["amount"] shouldEqual "1000"
        args["transaction_type"] shouldEqual SCAN_RECEIVE
    }

    @Test
    fun `test create destination SelectAccount`() {
        directionCreator.createDestinationSelectAccount() shouldBeInstanceOf NavBottomNavigationDirections.ActionGlobalSelectAccountFragment::class
    }

    @Test
    fun `test create destination Splash`() {
        directionCreator.createDestinationSplash() shouldBeInstanceOf NavBottomNavigationDirections.ActionGlobalSplashFragment::class

    }

    @Test
    fun `test create destination Feedback`() {
        val mockFeedback: Feedback = mock()
        directionCreator.createDestinationFeedback(mockFeedback) shouldBeInstanceOf NavBottomNavigationDirections.ActionGlobalFeedbackFragment::class
        directionCreator.createDestinationFeedback(mockFeedback).arguments["feedback"] shouldEqual mockFeedback
    }

    @Test
    fun `test create destination Loading`() {
        directionCreator.createDestinationLoading() shouldBeInstanceOf NavBottomNavigationDirections.ActionGlobalLoadingFragment::class
    }
}
