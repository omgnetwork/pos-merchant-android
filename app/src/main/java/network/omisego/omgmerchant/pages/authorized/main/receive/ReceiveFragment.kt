package network.omisego.omgmerchant.pages.authorized.main.receive

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.pagination.PaginationList
import kotlinx.android.synthetic.main.fragment_topup.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.BaseFragment
import network.omisego.omgmerchant.databinding.FragmentReceiveBinding
import network.omisego.omgmerchant.extensions.observeFor
import network.omisego.omgmerchant.extensions.provideMainFragmentViewModel
import network.omisego.omgmerchant.extensions.selectedToken
import network.omisego.omgmerchant.extensions.setError
import network.omisego.omgmerchant.extensions.setTokens
import network.omisego.omgmerchant.pages.authorized.main.MainViewModel

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

        with(mainViewModel) {
            observeFor(liveTokenAPIResult) {
                it.handle(
                    ::handleLoadTokens,
                    spinner::setError
                )
                spinner.selectedToken = viewModel.liveSelectedToken.value
            }
        }
    }

    private fun handleLoadTokens(tokens: PaginationList<Token>) {
        spinner.setTokens(tokens, viewModel.liveSelectedToken)
    }
}
