package network.omisego.omgmerchant.pages.unauthorized.signin

import android.databinding.DataBindingUtil
import android.hardware.fingerprint.FingerprintManager
import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.AdminAuthenticationToken
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.launch
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.BaseFragment
import network.omisego.omgmerchant.databinding.FragmentSignInBinding
import network.omisego.omgmerchant.extensions.findRootController
import network.omisego.omgmerchant.extensions.observeEventFor
import network.omisego.omgmerchant.extensions.observeFor
import network.omisego.omgmerchant.extensions.provideAndroidViewModel
import network.omisego.omgmerchant.helper.runOnM
import network.omisego.omgmerchant.helper.runOnMToP
import network.omisego.omgmerchant.helper.runOnP
import network.omisego.omgmerchant.extensions.scrollBottom
import network.omisego.omgmerchant.extensions.toast

class SignInFragment : BaseFragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: SignInViewModel
    private lateinit var fingerprintViewModel: FingerprintBottomSheetViewModel
    private var scanFingerprintDialog: FingerprintBottomSheet? = null

    override fun onProvideViewModel() {
        viewModel = provideAndroidViewModel()
        fingerprintViewModel = provideAndroidViewModel()
    }

    override fun onBindDataBinding() {
        binding.viewmodel = viewModel
        binding.emailValidator = viewModel.emailValidator
        binding.passwordValidator = viewModel.passwordValidator
        binding.setLifecycleOwner(this)
    }

    override fun onObserveLiveData() {
        observeFor(viewModel.liveToast) { it ->
            it?.let {
                toast(it)
            }
        }
        observeEventFor(viewModel.liveSignInAPIResult) {
            viewModel.hideLoading(getString(R.string.sign_in_button))
            it.handle(this::handleSignInSuccess, this::handleSignInError)
        }

        runOnP { subscribeSignInWithFingerprintP() }
        runOnMToP { subscribeSignInWithFingerprintBelowP() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sign_in,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.hasCredential()) {
            findRootController().navigate(R.id.action_sign_in_to_main)
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            scrollBottom()
        }

        btnSignIn.setOnClickListener { viewModel.signIn() }
        etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                signIn()
            }
            false
        }
    }

    private fun navigateToMain(data: AdminAuthenticationToken) {
        launch(Dispatchers.Main) {
            viewModel.saveCredential(data).await()
            viewModel.saveUserEmail(etEmail.text.toString())
            findRootController().navigate(R.id.action_sign_in_to_main)
        }
    }

    private fun signIn() {
        viewModel.showLoading(getString(R.string.sign_in_button_loading))
        viewModel.signIn()
    }

    @RequiresApi(P)
    private fun subscribeSignInWithFingerprintP() {
        with(viewModel) {
            observeFor(liveAuthenticationSucceeded) {
                if (viewModel.isFingerprintAvailable()) {
                    etEmail.setText(viewModel.loadUserEmail())
                    etPassword.setText(viewModel.loadUserPassword())
                    signIn()
                } else {
                    toast(getString(R.string.dialog_fingerprint_option_not_enabled))
                }
            }

            observeFor(liveAuthenticationError) {
                if (it?.first == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT || it?.first == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT_PERMANENT) {
                    toast(getString(R.string.dialog_fingerprint_error_too_many_attempt))
                }
            }
        }
    }

    private fun subscribeSignInWithFingerprintBelowP() {
        runOnM {
            observeFor(viewModel.liveShowPre28FingerprintDialog) {
                if (it == true) {
                    scanFingerprintDialog = FingerprintBottomSheet()
                    scanFingerprintDialog?.show(childFragmentManager, null)
                }
            }

            observeFor(fingerprintViewModel.liveAuthPass) {
                if (!viewModel.isFingerprintAvailable()) {
                    toast(getString(R.string.dialog_fingerprint_option_not_enabled))
                } else if (it == true) {
                    scanFingerprintDialog?.dismiss()
                    etEmail.setText(viewModel.loadUserEmail())
                    etPassword.setText(viewModel.loadUserPassword())
                    signIn()
                }
            }
        }
    }

    private fun handleSignInSuccess(data: AdminAuthenticationToken) {
        toast(getString(R.string.sign_in_success))
        navigateToMain(data)
    }

    private fun handleSignInError(error: APIError) {
        toast(error.description)
    }
}
