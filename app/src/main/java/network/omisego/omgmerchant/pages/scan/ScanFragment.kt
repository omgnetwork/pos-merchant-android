package network.omisego.omgmerchant.pages.scan

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.databinding.FragmentScanBinding

class ScanFragment : Fragment() {
    private lateinit var binding: FragmentScanBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_scan,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvAmount.text = arguments?.getString("amount")
    }
}
