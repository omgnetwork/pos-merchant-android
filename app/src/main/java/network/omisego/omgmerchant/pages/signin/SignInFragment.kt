package network.omisego.omgmerchant.pages.signin

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_sign_in.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.databinding.FragmentSignInBinding
import network.omisego.omgmerchant.extensions.logi
import network.omisego.omgmerchant.extensions.provideViewModel
import network.omisego.omgmerchant.extensions.runBelowM
import network.omisego.omgmerchant.extensions.runOnM
import network.omisego.omgmerchant.extensions.scrollBottom

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sign_in,
            container,
            false
        )
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            scrollBottom()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = provideViewModel()

        runBelowM {
            ivLogo.setImageDrawable(ContextCompat.getDrawable(ivLogo.context, R.drawable.omisego_logo_no_animated))
        }
        runOnM {
            startLogoAnimate()
        }
        setupDataBinding()
        subscribeSignInResult()
        ivLogo.setOnClickListener {
            runOnM {
                startLogoAnimate()
            }
        }
    }

    private fun subscribeSignInResult() {
        val (liveAuthenticationToken, liveAPIError) = viewModel.subscribeSignInResult()
        liveAuthenticationToken.observe(this, Observer {
            findNavController().navigate(R.id.action_sign_in_to_select_account)
        })
        liveAPIError.observe(this, Observer {
            logi(it)
        })
    }

    private fun setupDataBinding() {
        viewModel.liveEmail.observe(this, Observer {
            Log.d("SignIn", it)
        })
        binding.viewmodel = viewModel
        binding.setLifecycleOwner(this)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun startLogoAnimate() {
        val drawable = ivLogo.drawable
        if (drawable is AnimatedVectorDrawable) {
            drawable.start()
        }
    }
}
