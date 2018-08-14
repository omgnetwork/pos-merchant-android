package network.omisego.omgmerchant.pages.splash

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.databinding.FragmentSplashBinding
import network.omisego.omgmerchant.extensions.provideViewModel

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var viewModel: SplashViewModel
    private val handler by lazy { Handler() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_splash,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = provideViewModel()
        val account = viewModel.loadAccount()
        binding.account = account
    }

    override fun onStart() {
        super.onStart()
        handler.postDelayed({
            findNavController().navigateUp()
        }, 2000)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
    }
}
