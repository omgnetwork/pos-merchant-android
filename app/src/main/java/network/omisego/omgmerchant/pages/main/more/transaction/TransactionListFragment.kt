package network.omisego.omgmerchant.pages.main.more.transaction

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.pagination.PaginationList
import co.omisego.omisego.model.transaction.Transaction
import kotlinx.android.synthetic.main.fragment_select.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.LoadingRecyclerAdapter
import network.omisego.omgmerchant.custom.MarginDividerDecorator
import network.omisego.omgmerchant.databinding.FragmentTransactionListBinding
import network.omisego.omgmerchant.databinding.ViewholderTransactionBinding
import network.omisego.omgmerchant.extensions.dpToPx
import network.omisego.omgmerchant.extensions.provideAndroidViewModel
import network.omisego.omgmerchant.extensions.toast

class TransactionListFragment : Fragment() {
    private lateinit var binding: FragmentTransactionListBinding
    private lateinit var viewModel: TransactionListViewModel
    private lateinit var adapter: LoadingRecyclerAdapter<Transaction, ViewholderTransactionBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideAndroidViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_transaction_list,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadTransaction()
        subscribeTransactionInfo()
    }

    private fun loadTransaction() {
        adapter.addLoadingItems(10)
        viewModel.getTransaction().observe(this, Observer {
            it?.handle(this::handleLoadTransactionSuccess, this::handleLoadTransactionFail)
        })
    }

    private fun subscribeTransactionInfo() {
        viewModel.liveTransactionFailedDescription.observe(this, Observer {
            toast(it!!)
        })
    }

    private fun handleLoadTransactionSuccess(listTransaction: PaginationList<Transaction>) {
        adapter.addItems(listTransaction.data)
    }

    private fun handleLoadTransactionFail(error: APIError) {
        toast(error.description)
    }

    private fun setupRecyclerView() {
        adapter = LoadingRecyclerAdapter(R.layout.viewholder_account_loading, R.layout.viewholder_transaction, viewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        val paddingSize = context?.dpToPx(16f)!!
        val margin = Rect(paddingSize, 0, paddingSize, 0)
        recyclerView.addItemDecoration(MarginDividerDecorator(context!!, margin))
    }
}
