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
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.databinding.FragmentSignInBinding
import network.omisego.omgmerchant.extensions.logd
import network.omisego.omgmerchant.extensions.provideViewModel
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
        viewModel = provideViewModel()

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
        btnSignIn.setOnClickListener {
            val result = viewModel.signin()
            viewModel.showLoading(getString(R.string.sign_in_button_loading))
            result?.observe(this, Observer {
                viewModel.hideLoading(getString(R.string.sign_in_button))
                it?.solve(this::handleSignInSuccess, this::handleSignInError)
            })
        }
    }

    private fun handleSignInError(error: APIError) {
        logd(error)
        toast(error.description)
    }

    private fun handleSignInSuccess(data: AuthenticationToken) {
        toast(getString(R.string.sign_in_success, data.account.name))
        viewModel.saveCredential(data)
        findNavController().navigate(R.id.action_sign_in_to_select_account)
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
