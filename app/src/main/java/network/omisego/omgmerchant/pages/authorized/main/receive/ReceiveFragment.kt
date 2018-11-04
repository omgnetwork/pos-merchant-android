package network.omisego.omgmerchant.pages.authorized.main.receive

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_receive.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.BaseFragment
import network.omisego.omgmerchant.databinding.FragmentReceiveBinding
import network.omisego.omgmerchant.extensions.observeFor
import network.omisego.omgmerchant.extensions.provideMainFragmentViewModel
import network.omisego.omgmerchant.pages.authorized.main.MainViewModel
import network.omisego.omgmerchant.pages.authorized.main.shared.spinner.LiveTokenSpinner

class ReceiveFragment : BaseFragment() {
    private lateinit var binding: FragmentReceiveBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewModel: ReceiveViewModel

    override fun onProvideViewModel() {
        viewModel = provideMainFragmentViewModel()
        mainViewModel = provideMainFragmentViewModel()
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

    override fun onBindDataBinding() {
        binding.liveCalc = viewModel.liveCalculator
        binding.handler = viewModel.handler
        binding.decorator = viewModel.numberDecorator
        binding.setLifecycleOwner(this)
    }

    override fun onObserveLiveData() {
        activity?.observeFor(viewModel.liveCalculator) {
            mainViewModel.liveEnableNext.value = viewModel.shouldEnableNextButton()
        }

        viewModel.liveTokenSpinner = LiveTokenSpinner(
            spinner,
            viewModel,
            mainViewModel,
            "Can't load token"
        )
        viewModel.startListeningTokenSpinner()
    }
}
