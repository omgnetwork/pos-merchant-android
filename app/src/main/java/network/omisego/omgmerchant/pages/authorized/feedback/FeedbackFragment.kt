package network.omisego.omgmerchant.pages.authorized.feedback

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.BaseFragment
import network.omisego.omgmerchant.databinding.FragmentFeedbackBinding
import network.omisego.omgmerchant.extensions.provideAndroidViewModel

class FeedbackFragment : BaseFragment() {
    private lateinit var binding: FragmentFeedbackBinding
    private lateinit var viewModel: FeedbackViewModel

    override fun onProvideViewModel() {
        viewModel = provideAndroidViewModel()
    }

    override fun onReceiveArgs() {
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

    override fun onBindDataBinding() {
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        binding.tvDone.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onObserveLiveData() {
    }
}
