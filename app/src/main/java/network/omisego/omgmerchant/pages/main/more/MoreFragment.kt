package network.omisego.omgmerchant.pages.main.more

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.extensions.provideActivityAndroidViewModel
import network.omisego.omgmerchant.pages.main.more.account.SettingAccountFragment
import network.omisego.omgmerchant.pages.main.more.setting.SettingFragment
import network.omisego.omgmerchant.pages.main.more.setting.SettingViewModel

class MoreFragment : Fragment() {
    private lateinit var viewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = provideActivityAndroidViewModel()

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
                }
                null -> {
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.rootFragmentMore, SettingFragment())
                        .commit()
                }
            }
        })
    }
}
