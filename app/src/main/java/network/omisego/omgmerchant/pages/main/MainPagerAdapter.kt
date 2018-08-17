package network.omisego.omgmerchant.pages.main

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 7/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import network.omisego.omgmerchant.pages.main.more.MoreFragment
import network.omisego.omgmerchant.pages.main.receive.ReceiveFragment
import network.omisego.omgmerchant.pages.main.topup.TopupFragment

class MainPagerAdapter(
    fm: FragmentManager
) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            PAGE_RECEIVE -> ReceiveFragment()
            PAGE_TOPUP -> TopupFragment()
            PAGE_MORE -> MoreFragment()
            else -> throw IllegalStateException("MainPagerAdapter should not has position $position")
        }
    }

    override fun getCount() = PAGE_SIZE
}
