package network.omisego.omgmerchant.pages.authorized.confirm

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.BaseFragment
import network.omisego.omgmerchant.databinding.FragmentConfirmBinding
import network.omisego.omgmerchant.extensions.observeEventFor
import network.omisego.omgmerchant.extensions.provideMainFragmentAndroidViewModel
import network.omisego.omgmerchant.extensions.provideMainFragmentViewModel
import network.omisego.omgmerchant.pages.authorized.main.MainViewModel
import network.omisego.omgmerchant.pages.authorized.scan.QRPayloadViewModel

class ConfirmFragment : BaseFragment() {
    private lateinit var binding: FragmentConfirmBinding
    private lateinit var viewModel: ConfirmViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var qrPayloadViewModel: QRPayloadViewModel

    override fun onProvideViewModel() {
        viewModel = provideMainFragmentAndroidViewModel()
        qrPayloadViewModel = provideMainFragmentViewModel()
        mainViewModel = provideMainFragmentViewModel()
    }

    override fun onReceiveArgs() {
        viewModel.args = ConfirmFragmentArgs.fromBundle(arguments)
    }

    override fun onBindDataBinding() {
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
    }

    override fun onObserveLiveData() {
        with(viewModel) {
            observeEventFor(liveNoClick) { findNavController().navigateUp() }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm, container, false)
        return binding.root
    }
}
