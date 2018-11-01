package network.omisego.omgmerchant.pages.authorized.main.topup

import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_topup.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.databinding.FragmentTopupBinding
import network.omisego.omgmerchant.extensions.provideMainFragmentViewModel
import network.omisego.omgmerchant.pages.authorized.main.MainViewModel
import network.omisego.omgmerchant.pages.authorized.main.shared.spinner.LiveTokenSpinner
import network.omisego.omgmerchant.utils.NumberDecorator

class TopupFragment : Fragment() {
    private lateinit var binding: FragmentTopupBinding
    private lateinit var viewModel: TopupViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var calculatorObserver: Observer<String>

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        viewModel = provideMainFragmentViewModel()
        mainViewModel = provideMainFragmentViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_topup,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCalculator()
        setupDataBinding()
        viewModel.liveTokenSpinner = LiveTokenSpinner(
            spinner,
            viewModel,
            mainViewModel,
            "Can't load token"
        )
        viewModel.startListeningTokenSpinner()
    }

    private fun observeCalculator() {
        calculatorObserver = Observer {
            mainViewModel.liveEnableNext.value = it != "0"
        }
        viewModel.liveCalculator.observe(activity!!, calculatorObserver)
    }

    private fun setupDataBinding() {
        binding.liveCalc = viewModel.liveCalculator
        binding.handler = viewModel.handler
        binding.decorator = NumberDecorator()
        binding.setLifecycleOwner(this)
    }
}
