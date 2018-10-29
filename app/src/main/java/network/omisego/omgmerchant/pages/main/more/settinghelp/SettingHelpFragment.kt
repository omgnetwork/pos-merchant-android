package network.omisego.omgmerchant.pages.main.more.settinghelp

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_setting_help.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.LoadingRecyclerAdapter
import network.omisego.omgmerchant.databinding.FragmentSettingHelpBinding
import network.omisego.omgmerchant.databinding.ViewholderSettingHelpBinding
import network.omisego.omgmerchant.extensions.provideAndroidViewModel
import network.omisego.omgmerchant.extensions.toast

class SettingHelpFragment : Fragment() {
    private lateinit var binding: FragmentSettingHelpBinding
    private lateinit var viewModel: SettingHelpViewModel
    private lateinit var adapter: LoadingRecyclerAdapter<String, ViewholderSettingHelpBinding>
    private lateinit var confirmFingerprintDialog: ConfirmFingerprintDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideAndroidViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_setting_help,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        switchFingerprint.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && !viewModel.hasFingerprintPassword()) {
                confirmFingerprintDialog = ConfirmFingerprintDialog().apply {
                    setLiveConfirmSuccess(viewModel.liveAuthenticateSuccessful)
                }
                confirmFingerprintDialog.show(childFragmentManager, null)
            } else if (!isChecked) {
                viewModel.handleFingerprintOption(false)
                viewModel.deleteFingerprintCredential()
            }
        }

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.liveClickMenu.observe(this, Observer { it ->
            it?.let {
                toast(it)
            }
        })

        viewModel.liveAuthenticateSuccessful.observe(this, Observer {
            if (it == true) {
                confirmFingerprintDialog.dismiss()
                viewModel.handleFingerprintOption(true)
                toast(getString(R.string.setting_help_enable_finger_print_successfully))
            }
            switchFingerprint.isChecked = it!!
        })
    }

    private fun setupRecyclerView() {
        adapter = LoadingRecyclerAdapter(
            R.layout.viewholder_transaction_loading,
            R.layout.viewholder_setting_help,
            viewModel
        )
        adapter.addItems(viewModel.menus)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context!!)
    }
}
