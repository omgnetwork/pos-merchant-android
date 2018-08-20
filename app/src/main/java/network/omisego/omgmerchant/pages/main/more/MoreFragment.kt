package network.omisego.omgmerchant.pages.main.more

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_more.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.databinding.FragmentMoreBinding
import network.omisego.omgmerchant.extensions.provideAndroidViewModel

class MoreFragment : Fragment() {
    private lateinit var binding: FragmentMoreBinding
    private lateinit var viewModel: MoreViewModel
    private lateinit var adapter: MoreAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_more,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = provideAndroidViewModel()
        adapter = MoreAdapter(viewModel.menus)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }
}
