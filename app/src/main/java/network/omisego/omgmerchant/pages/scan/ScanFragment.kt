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
import co.omisego.omisego.qrcode.scanner.OMGQRScannerContract
import co.omisego.omisego.qrcode.scanner.SimpleVerifier
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.databinding.FragmentScanBinding
import network.omisego.omgmerchant.extensions.provideActivityViewModel
import network.omisego.omgmerchant.extensions.provideAndroidViewModel

class ScanFragment : Fragment() {
    private lateinit var binding: FragmentScanBinding
    private lateinit var viewModel: ScanViewModel
    private lateinit var addressViewModel: AddressViewModel
    private val verifier: OMGQRScannerContract.Preview.Verifier by lazy {
        SimpleVerifier(binding.scanner, addressViewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideAndroidViewModel()
        addressViewModel = provideActivityViewModel()
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

        /* Setup ViewModel */
        viewModel.args = ScanFragmentArgs.fromBundle(arguments)

        /* Binding */
        binding.viewModel = viewModel
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        addressViewModel.liveAddress.observe(this, Observer {
            if (it != null)
                findNavController().navigateUp()
        })
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
                    binding.scanner.startCameraWithVerifier(verifier)
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

    override fun onStart() {
        super.onStart()
        handleCameraPermission()
    }

    override fun onStop() {
        super.onStop()
        binding.scanner.stopCamera()
    }
}
