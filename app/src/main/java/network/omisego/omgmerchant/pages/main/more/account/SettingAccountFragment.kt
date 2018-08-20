package network.omisego.omgmerchant.pages.main.more.account

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Account
import co.omisego.omisego.model.pagination.PaginationList
import kotlinx.android.synthetic.main.fragment_setting_account.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.LiveRecyclerAdapter
import network.omisego.omgmerchant.base.StateRecyclerAdapter
import network.omisego.omgmerchant.databinding.FragmentSettingAccountBinding
import network.omisego.omgmerchant.extensions.provideViewModel
import network.omisego.omgmerchant.extensions.toast

class SettingAccountFragment : Fragment() {
    private lateinit var binding: FragmentSettingAccountBinding
    private lateinit var viewModel: SettingAccountViewModel
    private lateinit var adapter: LiveRecyclerAdapter<Account>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_setting_account,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = LiveRecyclerAdapter(viewModel.liveState)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        listen()
    }

    private fun listen() {
        adapter.startListening()
        viewModel.liveState.value = StateRecyclerAdapter.Loading(layout = R.layout.viewholder_account_loading)
        viewModel.loadAccounts().observe(this, Observer {
            it?.handle(
                this::handleLoadAccount,
                this::handleLoadAccountFail
            )
        })
        viewModel.liveAccountSelect.observe(this, Observer { account ->
            account?.let {
            }
        })
    }

    private fun handleLoadAccount(account: PaginationList<Account>) {
        viewModel.liveState.value = StateRecyclerAdapter.Show(
            account.data,
            R.layout.viewholder_setting_account,
            viewModel
        )
    }

    private fun handleLoadAccountFail(error: APIError) {
        toast(error.description)
    }
}
