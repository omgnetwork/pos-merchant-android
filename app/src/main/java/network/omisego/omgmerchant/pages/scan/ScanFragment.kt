package network.omisego.omgmerchant.pages.scan

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.transaction.Transaction
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

    private fun handleTransferSuccess(transaction: Transaction) {
        logi(transaction)
        viewModel.saveFeedback(transactionType, transaction)
        findNavController().navigateUp()
        // Save transaction in the share preference and clear when showing the success screen
    }

    private fun handleTransferFail(error: APIError) {
        logi(error)
        toast(error.description)
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
