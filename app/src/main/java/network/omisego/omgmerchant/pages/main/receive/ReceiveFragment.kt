package network.omisego.omgmerchant.pages.main.receive

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_receive.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.databinding.FragmentReceiveBinding

class ReceiveFragment : Fragment() {
    private lateinit var binding: FragmentReceiveBinding
    private lateinit var viewModel: ReceiveViewModel
    private val mockTokens = listOf("OMG", "BTC", "ETH")

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
        viewModel = ViewModelProviders.of(this)[ReceiveViewModel::class.java]
        setupDataBinding()
        setupSpinner()
    }

    private fun setupSpinner() {
        spinner.adapter = ArrayAdapter<String>(spinner.context, R.layout.spinner_dropdown_item, mockTokens)
    }

    private fun setupDataBinding() {
        binding.liveCalc = viewModel.liveCalculator
        binding.handler = viewModel.handler
        binding.setLifecycleOwner(this)
    }
}
