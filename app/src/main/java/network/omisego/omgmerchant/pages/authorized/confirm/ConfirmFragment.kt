package network.omisego.omgmerchant.pages.authorized.confirm

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.model.transaction.Transaction
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.databinding.FragmentConfirmBinding
import network.omisego.omgmerchant.extensions.logi
import network.omisego.omgmerchant.extensions.provideActivityViewModel
import network.omisego.omgmerchant.extensions.provideAndroidViewModel
import network.omisego.omgmerchant.extensions.provideMainFragmentViewModel
import network.omisego.omgmerchant.extensions.toast
import network.omisego.omgmerchant.livedata.EventObserver
import network.omisego.omgmerchant.pages.authorized.main.MainViewModel
import network.omisego.omgmerchant.pages.authorized.scan.AddressViewModel

class ConfirmFragment : Fragment() {
    private lateinit var binding: FragmentConfirmBinding
    private lateinit var viewModel: ConfirmViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var addressViewModel: AddressViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideAndroidViewModel()
        addressViewModel = provideActivityViewModel()
        mainViewModel = provideMainFragmentViewModel()
        viewModel.address = addressViewModel.liveAddress.value!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.args = ConfirmFragmentArgs.fromBundle(arguments)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
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

    override fun onStart() {
        super.onStart()
        viewModel.liveYesClick.observe(this, EventObserver {
            viewModel.transfer(viewModel.provideTransactionCreateParams(viewModel.address))
        })
        viewModel.liveNoClick.observe(this, EventObserver {
            findNavController().navigateUp()
        })
        viewModel.liveTransaction.observe(this, Observer {
            it?.handle(
                this::handleTransferSuccess,
                this::handleTransferFail
            )
        })
        viewModel.liveWallet.observe(this, Observer {
            it?.handle(
                this::handleGetWalletSuccess,
                this::handleGetWalletFailed
            )
        })
    }
}
