package network.omisego.omgmerchant.pages.main.more

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.model.MoreMenu

class MoreViewModel(
    val app: Application
) : AndroidViewModel(app) {
    val menus: List<MoreMenu> = listOf(
        MoreMenu("\uE918", app.getString(R.string.more_account)),
        MoreMenu("\uE921", app.getString(R.string.more_transaction)),
        MoreMenu("\uE91F", app.getString(R.string.more_setting_and_help))
    )
}
