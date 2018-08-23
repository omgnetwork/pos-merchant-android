package network.omisego.omgmerchant.pages.main.more.settinghelp

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

class SettingHelpFragment : Fragment() {
    private lateinit var binding: FragmentSettingHelpBinding
    private lateinit var viewModel: SettingHelpViewModel
    private lateinit var adapter: LoadingRecyclerAdapter<String, ViewholderSettingHelpBinding>

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
