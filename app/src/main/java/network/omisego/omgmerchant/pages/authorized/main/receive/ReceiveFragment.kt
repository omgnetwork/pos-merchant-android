package network.omisego.omgmerchant.pages.authorized.main.receive

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_receive.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.databinding.FragmentReceiveBinding
import network.omisego.omgmerchant.extensions.provideActivityViewModel
import network.omisego.omgmerchant.extensions.provideMainFragmentViewModel
import network.omisego.omgmerchant.pages.authorized.main.MainViewModel
import network.omisego.omgmerchant.pages.authorized.main.shared.spinner.LiveTokenSpinner

class ReceiveFragment : Fragment() {
    private lateinit var binding: FragmentReceiveBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewModel: ReceiveViewModel
    private val calculatorObserver by lazy {
        Observer<String> {
            mainViewModel.liveEnableNext.value = it != "0" && it?.indexOfAny(charArrayOf('-', '+')) == -1
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideMainFragmentViewModel()
        mainViewModel = provideActivityViewModel()
        viewModel.liveCalculator.observe(activity!!, calculatorObserver)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_receive,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDataBinding()
        viewModel.liveTokenSpinner = LiveTokenSpinner(
            spinner,
            viewModel,
            mainViewModel,
            "Can't load token"
        )
        viewModel.startListeningTokenSpinner()
    }

    private fun setupDataBinding() {
        binding.liveCalc = viewModel.liveCalculator
        binding.handler = viewModel.handler
        binding.decorator = viewModel.numberDecorator
        binding.setLifecycleOwner(this)
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.liveCalculator.removeObserver(calculatorObserver)
    }
}
