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
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.model.transaction.Transaction
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.BaseFragment
import network.omisego.omgmerchant.databinding.FragmentConfirmBinding
import network.omisego.omgmerchant.extensions.logi
import network.omisego.omgmerchant.extensions.observeEventFor
import network.omisego.omgmerchant.extensions.observeFor
import network.omisego.omgmerchant.extensions.provideActivityViewModel
import network.omisego.omgmerchant.extensions.provideAndroidViewModel
import network.omisego.omgmerchant.extensions.provideMainFragmentViewModel
import network.omisego.omgmerchant.extensions.toast
import network.omisego.omgmerchant.pages.authorized.main.MainViewModel
import network.omisego.omgmerchant.pages.authorized.scan.AddressViewModel

class ConfirmFragment : BaseFragment() {
    private lateinit var binding: FragmentConfirmBinding
    private lateinit var viewModel: ConfirmViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var addressViewModel: AddressViewModel

    override fun onProvideViewModel() {
        viewModel = provideAndroidViewModel()
        addressViewModel = provideActivityViewModel()
        mainViewModel = provideMainFragmentViewModel()
        viewModel.address = addressViewModel.liveAddress.value!!
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
            observeFor(liveTransaction) {
                it.handle(
                    this@ConfirmFragment::handleTransferSuccess,
                    this@ConfirmFragment::handleTransferFail
                )
            }
            observeFor(liveWallet) {
                it.handle(
                    this@ConfirmFragment::handleGetWalletSuccess,
                    this@ConfirmFragment::handleGetWalletFailed
                )
            }
            observeEventFor(liveNoClick) { findNavController().navigateUp() }
            observeEventFor(liveYesClick) { viewModel.transfer(viewModel.provideTransactionCreateParams(viewModel.address)) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm, container, false)
        return binding.root
    }

    private fun handleTransferSuccess(transaction: Transaction) {
        logi(transaction)
        mainViewModel.liveFeedback.value = viewModel.createFeedback(transaction)
    }

    private fun handleTransferFail(error: APIError) {
        viewModel.getUserWallet(viewModel.address)
        viewModel.error = error
        logi(error)
    }

    private fun handleGetWalletSuccess(wallet: Wallet) {
        mainViewModel.liveFeedback.value = viewModel.createFeedback(wallet)
    }

    private fun handleGetWalletFailed(error: APIError) {
        toast(error.description)
    }
}
