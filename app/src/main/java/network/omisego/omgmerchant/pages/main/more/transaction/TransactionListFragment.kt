package network.omisego.omgmerchant.pages.main.more.transaction

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.pagination.PaginationList
import co.omisego.omisego.model.transaction.Transaction
import kotlinx.android.synthetic.main.fragment_transaction_list.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.LoadingRecyclerAdapter
import network.omisego.omgmerchant.custom.MarginDividerDecorator
import network.omisego.omgmerchant.databinding.FragmentTransactionListBinding
import network.omisego.omgmerchant.databinding.ViewholderTransactionBinding
import network.omisego.omgmerchant.extensions.dpToPx
import network.omisego.omgmerchant.extensions.logi
import network.omisego.omgmerchant.extensions.provideAndroidViewModel
import network.omisego.omgmerchant.extensions.toast

class TransactionListFragment : Fragment() {
    private lateinit var binding: FragmentTransactionListBinding
    private lateinit var viewModel: TransactionListViewModel
    private lateinit var adapter: LoadingRecyclerAdapter<Transaction, ViewholderTransactionBinding>
    private val linearLayoutManager by lazy { LinearLayoutManager(context) }
    private var currentPage: Int = 1
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private var loading: Boolean = false
    private var isLastPage: Boolean = false
    private val dividerDecorator by lazy {
        val paddingSize = context?.dpToPx(16f)!!
        val margin = Rect(paddingSize, 0, paddingSize, 0)
        MarginDividerDecorator(context!!, margin)
    }

    companion object {
        val TOTAL_MOCK_LOADING_ITEM = 1
    }

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
        loadTransaction(currentPage)
        subscribeLoadMore()
        subscribeTransactionInfo()
        swipeRefresh.setOnRefreshListener {
            currentPage = 1
            isLastPage = false
            adapter.clearItems()
            loadTransaction(currentPage)
        }
    }

    private fun subscribeLoadMore() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayoutManager.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!loading && totalItemCount <= (lastVisibleItem + TOTAL_MOCK_LOADING_ITEM)) {
                    onLoadMore()
                }
            }
        })
    }

    private fun onLoadMore() {
        if (loading || isLastPage) return
        logi("Loadmore")
        loadTransaction(currentPage + 1)
    }

    private fun loadTransaction(page: Int) {
        recyclerView.visibility = View.VISIBLE
        recyclerView.removeItemDecoration(dividerDecorator)
        adapter.addLoadingItems(TOTAL_MOCK_LOADING_ITEM)
        loading = true
        viewModel.getTransaction(page).observe(this, Observer {
            it?.handle(this::handleLoadTransactionSuccess, this::handleLoadTransactionFail)
        })
    }

    private fun subscribeTransactionInfo() {
        viewModel.liveTransactionFailedDescription.observe(this, Observer {
            if (!it?.isEmpty()!!) {
                toast(it)
            }
        })
    }

    private fun handleLoadTransactionSuccess(listTransaction: PaginationList<Transaction>) {
        swipeRefresh.isRefreshing = false
        loading = false
        if (listTransaction.pagination.isLastPage && listTransaction.pagination.isFirstPage && listTransaction.data.isEmpty()) {
            recyclerView.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
        } else if (listTransaction.pagination.isLastPage && listTransaction.data.isEmpty()) {
            isLastPage = true
        } else {
            recyclerView.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE
        }
        currentPage = listTransaction.pagination.currentPage
        adapter.addItems(listTransaction.data)
    }

    private fun handleLoadTransactionFail(error: APIError) {
        toast(error.description)
        swipeRefresh.isRefreshing = false
        loading = false
    }

    private fun setupRecyclerView() {
        adapter = LoadingRecyclerAdapter(R.layout.viewholder_transaction_loading, R.layout.viewholder_transaction, viewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = linearLayoutManager
    }
}
