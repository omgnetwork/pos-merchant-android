package network.omisego.omgmerchant.extensions

import android.support.design.widget.TabLayout

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 7/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

operator fun TabLayout.get(index: Int) = this.getTabAt(index)
