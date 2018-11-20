package network.omisego.omgmerchant.pages.authorized.main.more.setting

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.core.executor.testing.InstantTaskExecutorRule
import network.omisego.omgmerchant.BuildConfig
import org.amshove.kluent.mock
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
class SettingViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val viewModel: SettingViewModel by lazy { SettingViewModel(RuntimeEnvironment.application, mock()) }

    @Test
    fun `test version name and endpoint should be retrieved correctly`() {
        viewModel.liveEndpoint.value shouldEqual BuildConfig.CLIENT_API_BASE_URL
        viewModel.liveVersion.value shouldEqual BuildConfig.VERSION_NAME
    }
}
