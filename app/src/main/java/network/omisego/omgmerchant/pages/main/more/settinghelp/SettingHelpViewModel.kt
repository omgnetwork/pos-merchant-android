package network.omisego.omgmerchant.pages.main.more.settinghelp

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 23/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.StateViewHolderBinding
import network.omisego.omgmerchant.databinding.ViewholderSettingHelpBinding

class SettingHelpViewModel(
    val app: Application
) : AndroidViewModel(app), StateViewHolderBinding<String, ViewholderSettingHelpBinding> {

    override fun bind(binding: ViewholderSettingHelpBinding, data: String) {
        binding.title = data
    }

    val menus: List<String> by lazy {
        listOf(
            app.getString(R.string.setting_help_faq),
            app.getString(R.string.setting_help_term_of_use),
            app.getString(R.string.setting_help_privacy)
        )
    }
}
