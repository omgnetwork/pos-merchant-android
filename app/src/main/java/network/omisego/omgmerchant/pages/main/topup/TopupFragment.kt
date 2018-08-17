package network.omisego.omgmerchant.pages.main.topup

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.pagination.PaginationList
import kotlinx.android.synthetic.main.fragment_receive.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.databinding.FragmentTopupBinding
import network.omisego.omgmerchant.extensions.provideActivityViewModel
import network.omisego.omgmerchant.extensions.toast
import network.omisego.omgmerchant.pages.main.MainViewModel
import network.omisego.omgmerchant.utils.NumberDecorator

class TopupFragment : Fragment() {
    private lateinit var binding: FragmentTopupBinding
    private lateinit var viewModel: TopupViewModel
    private lateinit var mainViewModel: MainViewModel
    private var tokenList: MutableList<Token> = mutableListOf()
    private val calculatorObserver = Observer<String> {
        mainViewModel.liveEnableNext.value = it != "0"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideActivityViewModel()
        mainViewModel = provideActivityViewModel()
        viewModel.liveCalculator.observe(this, calculatorObserver)
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
        setupDataBinding()
        setupSpinner()
        mainViewModel.liveTokenAPIResult.observe(this, Observer {
            it?.handle(this::handleSuccess, this::handleFail)
        })
    }

    private fun setupSpinner() {
        spinner.setOnItemSelectedListener { _, position, _, _ ->
            viewModel.liveToken.value = tokenList[position]
        }
    }

    private fun handleSuccess(tokens: PaginationList<Token>) {
        tokenList.addAll(tokens.data)
        viewModel.liveToken.value = tokenList[0]
        spinner.setItems(tokens.data.map { it.symbol.toUpperCase() })
    }

    private fun handleFail(error: APIError) {
        spinner.setItems("Can't load tokens")
        toast(error.description)
    }

    private fun setupDataBinding() {
        binding.liveCalc = viewModel.liveCalculator
        binding.handler = viewModel.handler
        binding.decorator = NumberDecorator()
        binding.setLifecycleOwner(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.liveCalculator.removeObserver(calculatorObserver)
    }
}
