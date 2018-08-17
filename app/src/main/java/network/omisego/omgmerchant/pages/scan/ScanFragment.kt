package network.omisego.omgmerchant.pages.scan

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.transaction.Transaction
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.databinding.FragmentScanBinding
import network.omisego.omgmerchant.extensions.logi
import network.omisego.omgmerchant.extensions.provideViewModel
import network.omisego.omgmerchant.extensions.toast

class ScanFragment : Fragment() {
    private var amount: Double = 0.0
    private var transactionType = "receive"
    private lateinit var token: Token
    private lateinit var binding: FragmentScanBinding
    private lateinit var viewModel: ScanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_scan,
            container,
            false
        )
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        token = arguments?.getParcelable("token") ?: return
        amount = arguments?.getDouble("amount") ?: 0.0
        transactionType = arguments?.getString("transaction_type") ?: transactionType
        viewModel.amount = amount
        viewModel.token = token
        binding.tvAmount.text = getString(R.string.scan_amount, amount, token.symbol)
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        handleCameraPermission()
    }

    private fun handleCameraPermission() {
        val snackbarPermissionListener = SnackbarOnDeniedPermissionListener.Builder
            .with(view, R.string.camera_permission_explain)
            .withOpenSettingsButton(R.string.permission_setting)
            .withCallback(object : Snackbar.Callback() {
                override fun onShown(snackbar: Snackbar?) {}
                override fun onDismissed(snackbar: Snackbar?, event: Int) {}
            })
            .build()

        Dexter.withActivity(activity!!)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    binding.scanner.startCameraWithVerifier(viewModel.verifier)
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    snackbarPermissionListener.onPermissionRationaleShouldBeShown(permission, token)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    snackbarPermissionListener.onPermissionDenied(response)
                }
            })
            .check()
    }

    private fun handleTransferSuccess(transaction: Transaction) {
        logi(transaction)
        viewModel.saveFeedback(transactionType, transaction)
        findNavController().navigateUp()
    }

    private fun handleTransferFail(error: APIError) {
        logi(error)
        toast(error.description)
    }

    override fun onStart() {
        super.onStart()
        viewModel.verifier.register()
        binding.scanner.startCameraWithVerifier(viewModel.verifier)
        viewModel.liveTransaction.observe(this, Observer {
            it?.handle(
                this::handleTransferSuccess,
                this::handleTransferFail
            )
        })
    }

    override fun onStop() {
        super.onStop()
        binding.scanner.stopCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.verifier.unregister()
    }
}
