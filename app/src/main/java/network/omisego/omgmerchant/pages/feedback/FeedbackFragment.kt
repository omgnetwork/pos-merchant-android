package network.omisego.omgmerchant.pages.feedback

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.databinding.FragmentFeedbackBinding
import network.omisego.omgmerchant.extensions.provideAndroidViewModel

class FeedbackFragment : Fragment() {
    private lateinit var binding: FragmentFeedbackBinding
    private lateinit var viewModel: FeedbackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideAndroidViewModel()
        viewModel.feedback = arguments?.getParcelable("feedback")!!
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
        binding.viewModel = viewModel
        binding.btnDone.setOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.deleteFeedback()
    }
}
