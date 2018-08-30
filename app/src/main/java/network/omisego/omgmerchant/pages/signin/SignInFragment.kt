package network.omisego.omgmerchant.pages.signin

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.AuthenticationToken
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.databinding.FragmentSignInBinding
import network.omisego.omgmerchant.extensions.logd
import network.omisego.omgmerchant.extensions.provideAndroidViewModel
import network.omisego.omgmerchant.extensions.runBelowM
import network.omisego.omgmerchant.extensions.runOnM
import network.omisego.omgmerchant.extensions.scrollBottom
import network.omisego.omgmerchant.extensions.toast

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
        viewModel = provideAndroidViewModel()

        runBelowM {
            ivLogo.setImageDrawable(ContextCompat.getDrawable(ivLogo.context, R.drawable.omisego_logo_no_animated))
        }
        runOnM {
            startLogoAnimate()
        }
        setupDataBinding()
        ivLogo.setOnClickListener {
            runOnM {
                startLogoAnimate()
            }
        }
        btnSignIn.setOnClickListener { _ ->
            signIn()
        }

        viewModel.liveToast.observe(this, Observer { it ->
            it?.let {
                toast(it)
            }
        })

        viewModel.liveAuthenticationSucceeded.observe(this, Observer {
            if (viewModel.isFingerprintAvailable()) {
                etEmail.setText(viewModel.loadUserEmail())
                etPassword.setText(viewModel.loadUserPassword())
                signIn()
            } else {
                toast("Please enable fingerprint option before use.")
            }
        })

        viewModel.liveAuthenticationError.observe(this, Observer {
            toast("Authentication error")
        })

        viewModel.liveAuthenticationFailed.observe(this, Observer {
            toast("Authentication failed")
        })

        viewModel.liveAuthenticationHelp.observe(this, Observer {
            toast("Authentication helped")
        })
    }

    private fun signIn() {
        viewModel.signin()?.let { liveResult ->
            viewModel.showLoading(getString(R.string.sign_in_button_loading))
            liveResult.observe(this, Observer {
                viewModel.hideLoading(getString(R.string.sign_in_button))
                it?.handle(this::handleSignInSuccess, this::handleSignInError)
            })
        }
    }

    private fun handleSignInError(error: APIError) {
        logd(error)
        toast(error.description)
    }

    private fun handleSignInSuccess(data: AuthenticationToken) {
        toast(getString(R.string.sign_in_success, data.account.name))
        launch(UI) {
            viewModel.saveCredential(data).await()
            viewModel.saveUserEmail(etEmail.text.toString())
            findNavController().navigateUp()
        }
    }

    private fun setupDataBinding() {
        binding.viewmodel = viewModel
        binding.emailValidator = viewModel.emailValidator
        binding.passwordValidator = viewModel.passwordValidator
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
