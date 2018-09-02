package network.omisego.omgmerchant.pages.signin

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.graphics.drawable.AnimatedVectorDrawable
import android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_LOCKOUT
import android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_LOCKOUT_PERMANENT
import android.os.Build
import android.os.Build.VERSION_CODES.P
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
import network.omisego.omgmerchant.extensions.provideAndroidViewModel
import network.omisego.omgmerchant.extensions.runBelowM
import network.omisego.omgmerchant.extensions.runBelowP
import network.omisego.omgmerchant.extensions.runOnM
import network.omisego.omgmerchant.extensions.runOnP
import network.omisego.omgmerchant.extensions.scrollBottom
import network.omisego.omgmerchant.extensions.toast

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: SignInViewModel
    private lateinit var fingerprintViewModel: FingerprintBottomSheetViewModel
    private var scanFingerprintDialog: FingerprintBottomSheet? = null

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
        fingerprintViewModel = provideAndroidViewModel()
        runBelowM {
            ivLogo.setImageDrawable(ContextCompat.getDrawable(ivLogo.context, R.drawable.omisego_logo_no_animated))
        }
        runOnM { startLogoAnimate() }

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

        runOnP { subscribeSignInWithFingerprintP() }
        runBelowP { subscribeSignInWithFingerprintBelowP() }
    }

    private fun signIn() {
        viewModel.signIn()?.let { liveResult ->
            viewModel.showLoading(getString(R.string.sign_in_button_loading))
            liveResult.observe(this, Observer {
                viewModel.hideLoading(getString(R.string.sign_in_button))
                it?.handle(this::handleSignInSuccess, this::handleSignInError)
            })
        }
    }

    @RequiresApi(P)
    private fun subscribeSignInWithFingerprintP() {
        viewModel.liveAuthenticationSucceeded.observe(this, Observer {
            if (viewModel.isFingerprintAvailable()) {
                etEmail.setText(viewModel.loadUserEmail())
                etPassword.setText(viewModel.loadUserPassword())
                signIn()
            } else {
                toast(getString(R.string.dialog_fingerprint_option_not_enabled))
            }
        })

        viewModel.liveAuthenticationError.observe(this, Observer {
            if (it?.first == FINGERPRINT_ERROR_LOCKOUT || it?.first == FINGERPRINT_ERROR_LOCKOUT_PERMANENT) {
                toast(getString(R.string.dialog_fingerprint_error_too_many_attempt))
            }
        })
    }

    private fun subscribeSignInWithFingerprintBelowP() {
        runOnM {
            viewModel.liveShowPre28FingerprintDialog.observe(this, Observer { it ->
                if (it == true) {
                    scanFingerprintDialog = FingerprintBottomSheet()
                    scanFingerprintDialog?.show(childFragmentManager, null)
                }
            })

            fingerprintViewModel.liveAuthPass.observe(this, Observer {
                if (!viewModel.isFingerprintAvailable()) {
                    toast(getString(R.string.dialog_fingerprint_option_not_enabled))
                } else if (it == true) {
                    scanFingerprintDialog?.dismiss()
                    etEmail.setText(viewModel.loadUserEmail())
                    etPassword.setText(viewModel.loadUserPassword())
                    signIn()
                }
            })
        }
    }

    private fun proceed(data: AuthenticationToken) {
        launch(UI) {
            viewModel.saveCredential(data).await()
            viewModel.saveUserEmail(etEmail.text.toString())
            findNavController().navigateUp()
        }
    }

    private fun handleSignInSuccess(data: AuthenticationToken) {
        toast(getString(R.string.sign_in_success, data.account.name))
        proceed(data)
    }

    private fun handleSignInError(error: APIError) {
        toast(error.description)
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
