package network.omisego.omgmerchant.pages.main.more.setting

import android.databinding.DataBindingUtil
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_setting.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.custom.MarginDividerDecorator
import network.omisego.omgmerchant.databinding.FragmentSettingBinding
import network.omisego.omgmerchant.extensions.dpToPx
import network.omisego.omgmerchant.extensions.provideActivityAndroidViewModel

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var viewModel: SettingViewModel
    private lateinit var adapter: SettingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_setting,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = provideActivityAndroidViewModel()
        binding.viewmodel = viewModel
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = SettingAdapter(viewModel, viewModel.menus)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        val margin = Rect(context?.dpToPx(72f)!!, 0, 0, 0)
        recyclerView.addItemDecoration(MarginDividerDecorator(context!!, margin))

    }

}
