package network.omisego.omgmerchant.pages.feedback

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.transaction.Transaction
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.databinding.FragmentFeedbackBinding
import network.omisego.omgmerchant.extensions.logi
import network.omisego.omgmerchant.extensions.provideAndroidViewModel
import network.omisego.omgmerchant.extensions.toast

class FeedbackFragment : Fragment() {
    private lateinit var binding: FragmentFeedbackBinding
    private lateinit var viewModel: FeedbackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideAndroidViewModel()
        viewModel.liveFeedback.value = FeedbackFragmentArgs.fromBundle(arguments).feedback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_feedback,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.deletePersistenceFeedback()
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        binding.tvDone.setOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.liveTransaction.observe(this, Observer {
            viewModel.liveLoading.value = false
            it?.handle(
                this::handleTransferSuccess,
                this::handleTransferFail
            )
        })
    }

    private fun handleTransferSuccess(transaction: Transaction) {
        viewModel.setFeedback(viewModel.liveFeedback.value!!.transactionType, transaction)
    }

    private fun handleTransferFail(error: APIError) {
        logi(error)
        toast(error.description)
    }
}
