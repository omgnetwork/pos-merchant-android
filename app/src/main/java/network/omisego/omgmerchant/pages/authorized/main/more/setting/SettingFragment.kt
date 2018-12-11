package network.omisego.omgmerchant.pages.authorized.main.more.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_setting.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.BaseFragment
import network.omisego.omgmerchant.custom.CustomRecyclerMarginDivider
import network.omisego.omgmerchant.databinding.FragmentSettingBinding
import network.omisego.omgmerchant.extensions.findChildController
import network.omisego.omgmerchant.extensions.findRootController
import network.omisego.omgmerchant.extensions.observeEventFor
import network.omisego.omgmerchant.extensions.provideAndroidViewModel
import network.omisego.omgmerchant.extensions.provideMarginLeft

class SettingFragment : BaseFragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var viewModel: SettingViewModel
    private lateinit var adapter: SettingAdapter

    override fun onProvideViewModel() {
        viewModel = provideAndroidViewModel()
    }

    override fun onBindDataBinding() {
        binding.viewmodel = viewModel
    }

    override fun onObserveLiveData() {
        with(viewModel) {
            observeEventFor(getLiveMenu()) {
                val destinationId = when (it.title) {
                    getString(R.string.more_account) -> R.id.action_more_to_settingAccountFragment
                    getString(R.string.more_transaction) -> R.id.action_more_to_transactionListFragment
                    getString(R.string.more_setting_and_help) -> R.id.action_more_to_settingHelpFragment
                    else -> throw IllegalStateException("Invalid destination $it")
                }

                findChildController().navigate(destinationId)
            }
            observeEventFor(liveSignOut) {
                findRootController().popBackStack()
                findRootController().navigate(R.id.action_global_sign_in)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_setting,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = SettingAdapter(viewModel, viewModel.menus)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(CustomRecyclerMarginDivider(context!!, context!!.provideMarginLeft(72)))
    }
}
