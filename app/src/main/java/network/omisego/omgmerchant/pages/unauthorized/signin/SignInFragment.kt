package network.omisego.omgmerchant.pages.unauthorized.signin

import android.hardware.fingerprint.FingerprintManager
import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.AdminAuthenticationToken
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.BaseFragment
import network.omisego.omgmerchant.databinding.FragmentSignInBinding
import network.omisego.omgmerchant.extensions.findRootController
import network.omisego.omgmerchant.extensions.observeEventFor
import network.omisego.omgmerchant.extensions.observeFor
import network.omisego.omgmerchant.extensions.provideAndroidViewModel
import network.omisego.omgmerchant.extensions.scrollBottom
import network.omisego.omgmerchant.extensions.toast
import network.omisego.omgmerchant.helper.runOnM
import network.omisego.omgmerchant.helper.runOnMToP
import network.omisego.omgmerchant.helper.runOnP
import kotlin.coroutines.CoroutineContext

class SignInFragment : BaseFragment(), CoroutineScope {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: SignInViewModel
    private lateinit var fingerprintViewModel: FingerprintBottomSheetViewModel
    private var scanFingerprintDialog: FingerprintBottomSheet? = null
    private val uiScope by lazy { CoroutineScope(Dispatchers.Main) }
    private val job by lazy { Job() }
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

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
            it.handle(this::handleSignInSuccess, this::handleSignInError)
            viewModel.hideLoading(getString(R.string.sign_in_button))
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

        btnSignIn.setOnClickListener { signIn() }
        etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                signIn()
            }
            false
        }
    }

    private fun navigateToMain(data: AdminAuthenticationToken) {
        launch {
            async(Dispatchers.IO) {
                viewModel.saveUserEmail(etEmail.text.toString())
                viewModel.saveCredential(data)
            }
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
            liveAuthenticationSucceeded.observe(this@SignInFragment, Observer {
                if (viewModel.isFingerprintAvailable()) {
                    etEmail.setText(viewModel.loadUserEmail())
                    etPassword.setText(viewModel.loadUserPassword())
                    this@SignInFragment.signIn()
                } else {
                    toast(getString(R.string.dialog_fingerprint_option_not_enabled))
                }
            })

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
