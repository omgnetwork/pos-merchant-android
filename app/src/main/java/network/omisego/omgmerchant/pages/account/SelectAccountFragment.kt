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
import androidx.navigation.fragment.findNavController
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Account
import co.omisego.omisego.model.pagination.PaginationList
import kotlinx.android.synthetic.main.fragment_select.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.LoadingRecyclerAdapter
import network.omisego.omgmerchant.databinding.FragmentSelectBinding
import network.omisego.omgmerchant.databinding.ViewholderAccountBinding
import network.omisego.omgmerchant.extensions.provideViewModel
import network.omisego.omgmerchant.extensions.toast

class SelectAccountFragment : Fragment() {
    private lateinit var binding: FragmentSelectBinding
    private lateinit var viewModel: SelectAccountViewModel
    private lateinit var adapter: LoadingRecyclerAdapter<Account, ViewholderAccountBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()
    }

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
        adapter = LoadingRecyclerAdapter(R.layout.viewholder_account_loading, R.layout.viewholder_account, viewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        listen()
    }

    private fun listen() {
        adapter.addLoadingItems(10)
        viewModel.loadAccounts().observe(this, Observer {
            it?.handle(
                this::handleLoadAccount,
                this::handleLoadAccountFail
            )
        })
        viewModel.liveAccountSelect.observe(this, Observer { account ->
            account?.let {
                findNavController().navigateUp()
            }
        })
    }

    private fun handleLoadAccount(account: PaginationList<Account>) {
        adapter.addItems(account.data)
    }

    private fun handleLoadAccountFail(error: APIError) {
        toast(error.description)
    }
}
