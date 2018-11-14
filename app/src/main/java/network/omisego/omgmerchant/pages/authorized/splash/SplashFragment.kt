package network.omisego.omgmerchant.pages.authorized.splash

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.BaseFragment
import network.omisego.omgmerchant.databinding.FragmentSplashBinding
import network.omisego.omgmerchant.extensions.provideAndroidViewModel

class SplashFragment : BaseFragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var viewModel: SplashViewModel
    private val handler by lazy { Handler() }

    override fun onProvideViewModel() {
        viewModel = provideAndroidViewModel()
    }

    override fun onBindDataBinding() {
        val account = viewModel.loadAccount()
        viewModel.loadWalletAndSave()
        binding.account = account
        binding.viewModel = viewModel
    }

    override fun onObserveLiveData() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_splash,
            container,
            false
        )
        return binding.root
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
