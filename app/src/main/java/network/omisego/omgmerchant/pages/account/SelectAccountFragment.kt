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
import co.omisego.omisego.model.Account
import kotlinx.android.synthetic.main.fragment_select.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.LiveRecyclerAdapter
import network.omisego.omgmerchant.databinding.FragmentSelectBinding
import network.omisego.omgmerchant.extensions.provideViewModel

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
        val (success, fail) = viewModel.subscribeLoadAccounts()
        success.observe(this, Observer {
            viewModel.changeState(it!!)
        })
        viewModel.loadAccounts()
//            Navigation.findNavController(it).navigate(R.id.action_nav_graph_self)
    }
}
