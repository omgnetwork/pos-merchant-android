package network.omisego.omgmerchant.pages.main.more

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.extensions.provideActivityAndroidViewModel
import network.omisego.omgmerchant.extensions.replaceFragment
import network.omisego.omgmerchant.extensions.replaceFragmentBackstack
import network.omisego.omgmerchant.pages.main.ToolbarViewModel
import network.omisego.omgmerchant.pages.main.more.account.SettingAccountFragment
import network.omisego.omgmerchant.pages.main.more.setting.SettingFragment
import network.omisego.omgmerchant.pages.main.more.setting.SettingViewModel
import network.omisego.omgmerchant.pages.main.more.settinghelp.SettingHelpFragment
import network.omisego.omgmerchant.pages.main.more.transaction.TransactionListFragment

class MoreFragment : Fragment() {
    private lateinit var viewModel: SettingViewModel
    private lateinit var toolbarViewModel: ToolbarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbarViewModel = provideActivityAndroidViewModel()
        viewModel = provideActivityAndroidViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replaceFragment(R.id.rootFragmentMore, SettingFragment())
        viewModel.getLiveMenu().observe(this, Observer {
            when (it) {
                viewModel.menus[0] -> {
                    replaceFragmentBackstack(R.id.rootFragmentMore, SettingAccountFragment())
                    toolbarViewModel.setToolbarTitle(getString(R.string.account_title))
                }
                viewModel.menus[1] -> {
                    replaceFragmentBackstack(R.id.rootFragmentMore, TransactionListFragment())
                    toolbarViewModel.setToolbarTitle(getString(R.string.transaction_list_title))
                }
                viewModel.menus[2] -> {
                    replaceFragmentBackstack(R.id.rootFragmentMore, SettingHelpFragment())
                    toolbarViewModel.setToolbarTitle(getString(R.string.setting_help_title))
                }
                null -> {
                    replaceFragment(R.id.rootFragmentMore, SettingFragment())
                    toolbarViewModel.setToolbarTitle(getString(R.string.more_title))
                }
            }
        })
    }
}
