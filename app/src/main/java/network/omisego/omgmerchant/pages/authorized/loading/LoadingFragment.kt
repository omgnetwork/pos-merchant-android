package network.omisego.omgmerchant.pages.authorized.loading

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.BaseFragment
import network.omisego.omgmerchant.databinding.FragmentLoadingBinding
import network.omisego.omgmerchant.extensions.provideMainFragmentViewModel
import network.omisego.omgmerchant.livedata.EventObserver

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class LoadingFragment : BaseFragment() {
    private lateinit var binding: FragmentLoadingBinding
    private lateinit var viewModel: LoadingViewModel

    override fun onProvideViewModel() {
        viewModel = provideMainFragmentViewModel()
        viewModel.liveTransactionConsumptionCancelId.value = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_loading,
            container,
            false
        )
        return binding.root
    }

    override fun onBindDataBinding() {
        super.onBindDataBinding()
        binding.viewModel = viewModel
    }

    override fun onObserveLiveData() {
        viewModel.liveTransactionConsumptionRejectResult.observe(this, EventObserver {
            it.handle(viewModel::handleRejectTransactionConsumptionSuccess, viewModel::handleRejectTransactionConsumptionFailed)
        })
    }
}
