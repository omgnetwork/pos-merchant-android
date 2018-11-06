package network.omisego.omgmerchant.pages.authorized.scan

import android.Manifest
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.BaseFragment
import network.omisego.omgmerchant.databinding.FragmentScanBinding
import network.omisego.omgmerchant.extensions.provideAndroidViewModel
import network.omisego.omgmerchant.extensions.provideMainFragmentViewModel

class ScanFragment : BaseFragment() {
    private lateinit var binding: FragmentScanBinding
    private lateinit var viewModel: ScanViewModel
    private lateinit var qrPayloadViewModel: QRPayloadViewModel

    override fun onReceiveArgs() {
        viewModel.args = ScanFragmentArgs.fromBundle(arguments)
    }

    override fun onProvideViewModel() {
        viewModel = provideAndroidViewModel()
        qrPayloadViewModel = provideMainFragmentViewModel()
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

    override fun onBindDataBinding() {
        binding.viewModel = viewModel
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onObserveLiveData() {}

    override fun onStart() {
        super.onStart()
        handleCameraPermission()
    }

    override fun onStop() {
        super.onStop()
        binding.scanner.stopCamera()
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
                    binding.scanner.startCameraWithVerifier(qrPayloadViewModel)
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
}
