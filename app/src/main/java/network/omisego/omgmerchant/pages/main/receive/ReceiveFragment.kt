package network.omisego.omgmerchant.pages.main.receive

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.databinding.FragmentReceiveBinding

class ReceiveFragment : Fragment() {
    private lateinit var binding: FragmentReceiveBinding

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
        val viewModel = ViewModelProviders.of(this)[ReceiveViewModel::class.java]
        binding.liveCalc = viewModel.liveCalculator
        binding.handler = viewModel.handler
        binding.setLifecycleOwner(this)
    }
}
