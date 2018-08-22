package network.omisego.omgmerchant.pages.main.more

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.extensions.provideActivityAndroidViewModel
import network.omisego.omgmerchant.pages.main.ToolbarViewModel
import network.omisego.omgmerchant.pages.main.more.account.SettingAccountFragment
import network.omisego.omgmerchant.pages.main.more.setting.SettingFragment
import network.omisego.omgmerchant.pages.main.more.setting.SettingViewModel
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

        childFragmentManager
            .beginTransaction()
            .replace(R.id.rootFragmentMore, SettingFragment())
            .commit()

        viewModel.liveMenu.observe(this, Observer {
            when (it) {
                viewModel.menus[0] -> {
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.rootFragmentMore, SettingAccountFragment())
                        .commit()
                    toolbarViewModel.liveToolbarText.value = getString(R.string.account_title)
                }
                viewModel.menus[1] -> {
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.rootFragmentMore, TransactionListFragment())
                        .commit()
                    toolbarViewModel.liveToolbarText.value = getString(R.string.transaction_list_title)
                }
                null -> {
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.rootFragmentMore, SettingFragment())
                        .commit()
                    toolbarViewModel.liveToolbarText.value = getString(R.string.more_title)
                }
            }
        })
    }
}
