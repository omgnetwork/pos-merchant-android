package network.omisego.omgmerchant.pages.account

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Account
import co.omisego.omisego.model.pagination.PaginationList
import kotlinx.android.synthetic.main.fragment_select.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.LiveRecyclerAdapter
import network.omisego.omgmerchant.base.StateRecyclerAdapter
import network.omisego.omgmerchant.databinding.FragmentSelectBinding
import network.omisego.omgmerchant.extensions.provideViewModel
import network.omisego.omgmerchant.extensions.toast

class SelectAccountFragment : Fragment() {
    private lateinit var binding: FragmentSelectBinding
    private lateinit var viewModel: SelectAccountViewModel
    private lateinit var adapter: LiveRecyclerAdapter<Account>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_select,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = provideViewModel()
        adapter = LiveRecyclerAdapter(viewModel.liveState)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        viewModel.loadAccounts().observe(this, Observer {
            it?.handle(
                this::handleLoadAccount,
                this::handleLoadAccountFail
            )
        })
        viewModel.liveAccountSelect.observe(this, Observer {
            it?.let {
                Navigation.findNavController(this.rootView).navigate(R.id.action_nav_graph_self)
            }
        })
    }

    private fun handleLoadAccount(account: PaginationList<Account>) {
        viewModel.liveState.value = StateRecyclerAdapter.Show(
            account.data,
            R.layout.viewholder_account,
            viewModel
        )
    }

    private fun handleLoadAccountFail(error: APIError) {
        toast(error.description)
    }
}
