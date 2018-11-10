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
import network.omisego.omgmerchant.base.BaseCalculatorFragment
import network.omisego.omgmerchant.databinding.FragmentReceiveBinding
import network.omisego.omgmerchant.extensions.provideMainFragmentAndroidViewModel
import network.omisego.omgmerchant.extensions.selectedToken
import network.omisego.omgmerchant.extensions.setTokens

class ReceiveFragment : BaseCalculatorFragment() {
    private lateinit var binding: FragmentReceiveBinding
    private lateinit var viewModel: ReceiveViewModel

    override fun onProvideViewModel() {
        super.onProvideViewModel()
        viewModel = provideMainFragmentAndroidViewModel()
        setupBehavior(viewModel)
    }

    override fun onBindDataBinding() {
        binding.liveCalc = viewModel.liveCalculator
        binding.handler = viewModel.handler
        binding.decorator = viewModel.helperNumberFormatter
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
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

    override fun onSetTokens(tokens: PaginationList<Token>) {
        spinner.setTokens(tokens, viewModel.liveSelectedToken)
    }

    override fun onSetSelectedToken(token: Token?) {
        spinner.selectedToken = token
    }
}
