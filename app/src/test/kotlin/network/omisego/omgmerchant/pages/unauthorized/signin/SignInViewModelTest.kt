package network.omisego.omgmerchant.pages.unauthorized.signin

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.mock
import network.omisego.omgmerchant.BuildConfig
import org.amshove.kluent.shouldEqual
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23])
class SignInViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val viewModel: SignInViewModel by lazy { SignInViewModel(RuntimeEnvironment.application, mock(), mock()) }

    @Test
    fun `test version should be retrieved correctly`() {
        viewModel.liveVersion.value shouldEqual BuildConfig.VERSION_NAME
    }
}
