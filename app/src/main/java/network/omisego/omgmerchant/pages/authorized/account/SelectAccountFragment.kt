package network.omisego.omgmerchant.pages.authorized.account

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Account
import co.omisego.omisego.model.pagination.PaginationList
import kotlinx.android.synthetic.main.fragment_select.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.BaseFragment
import network.omisego.omgmerchant.base.LoadingRecyclerAdapter
import network.omisego.omgmerchant.custom.MarginDividerDecorator
import network.omisego.omgmerchant.databinding.FragmentSelectBinding
import network.omisego.omgmerchant.databinding.ViewholderAccountBinding
import network.omisego.omgmerchant.extensions.findChildController
import network.omisego.omgmerchant.extensions.observeEventFor
import network.omisego.omgmerchant.extensions.observeFor
import network.omisego.omgmerchant.extensions.provideViewModel
import network.omisego.omgmerchant.extensions.toast
import network.omisego.omgmerchant.utils.provideMarginLeft

class SelectAccountFragment : BaseFragment() {
    private lateinit var binding: FragmentSelectBinding
    private lateinit var viewModel: SelectAccountViewModel
    private lateinit var adapter: LoadingRecyclerAdapter<Account, ViewholderAccountBinding>
    private lateinit var dividerDecorator: MarginDividerDecorator

    override fun onProvideViewModel() {
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
        dividerDecorator = MarginDividerDecorator(context!!, context!!.provideMarginLeft())

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(dividerDecorator)

        adapter.addLoadingItems(10)
        viewModel.loadAccounts()
    }

    override fun onObserveLiveData() {
        with(viewModel) {
            observeEventFor(liveAccountList) {
                it.handle(
                    this@SelectAccountFragment::handleLoadAccount,
                    this@SelectAccountFragment::handleLoadAccountFail
                )
            }
            observeFor(liveAccountSelect) {
                findChildController().popBackStack()
                findChildController().navigate(R.id.action_global_splashFragment)
            }
        }
    }

    private fun handleLoadAccount(account: PaginationList<Account>) {
        adapter.addItems(account.data)
    }

    private fun handleLoadAccountFail(error: APIError) {
        toast(error.description)
    }
}
