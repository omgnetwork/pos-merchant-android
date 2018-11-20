package network.omisego.omgmerchant.pages.authorized.main.topup

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
import network.omisego.omgmerchant.databinding.FragmentTopupBinding
import network.omisego.omgmerchant.extensions.provideMainFragmentAndroidViewModel
import network.omisego.omgmerchant.extensions.selectedToken
import network.omisego.omgmerchant.extensions.setTokens

class TopupFragment : BaseCalculatorFragment() {
    private lateinit var binding: FragmentTopupBinding
    private lateinit var viewModel: TopupViewModel

    override fun onProvideViewModel() {
        super.onProvideViewModel()
        viewModel = provideMainFragmentAndroidViewModel()
        setupBehavior(viewModel)
    }

    override fun onBindDataBinding() {
        binding.liveCalc = viewModel.liveCalculator
        binding.handler = viewModel.handler
        binding.formatter = viewModel.formatter
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
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

    override fun onSetSelectedToken(token: Token?) {
        spinner.selectedToken = token
    }

    override fun onSetTokens(tokens: PaginationList<Token>) {
        spinner.setTokens(tokens, viewModel.liveSelectedToken)
    }
}
