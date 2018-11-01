package network.omisego.omgmerchant.pages.authorized.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_main.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.databinding.FragmentMainBinding
import network.omisego.omgmerchant.extensions.findChildController
import network.omisego.omgmerchant.extensions.observeEventFor
import network.omisego.omgmerchant.extensions.observeFor
import network.omisego.omgmerchant.extensions.provideActivityViewModel
import network.omisego.omgmerchant.extensions.provideMainFragmentViewModel
import network.omisego.omgmerchant.pages.authorized.main.receive.ReceiveViewModel
import network.omisego.omgmerchant.pages.authorized.main.topup.TopupViewModel
import network.omisego.omgmerchant.pages.authorized.scan.AddressViewModel

class MainFragment : Fragment() {

    /* ViewModel */
    private lateinit var receiveViewModel: ReceiveViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var topupViewModel: TopupViewModel
    private lateinit var addressViewModel: AddressViewModel

    /* Local */
    private lateinit var binding: FragmentMainBinding
    private var menuNext: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addressViewModel = provideActivityViewModel()
        mainViewModel = provideMainFragmentViewModel()
        receiveViewModel = provideMainFragmentViewModel()
        topupViewModel = provideMainFragmentViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupNavigationUI()
        subscribeToLiveData()
        mainViewModel.getTokens()
        mainViewModel.decideDestination()
    }

    override fun onStart() {
        super.onStart()
        with(findChildController()) {
            addOnNavigatedListener(mainViewModel.fullScreenNavigatedListener)
            addOnNavigatedListener(mainViewModel.nextButtonNavigatedListener)
            addOnNavigatedListener(mainViewModel.calculatorModeNavigatedListener)
        }
    }

    override fun onStop() {
        with(findChildController()) {
            removeOnNavigatedListener(mainViewModel.calculatorModeNavigatedListener)
            removeOnNavigatedListener(mainViewModel.fullScreenNavigatedListener)
            removeOnNavigatedListener(mainViewModel.nextButtonNavigatedListener)
        }
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)

        /* Restore the next button state */
        menuNext = menu?.findItem(R.id.next).apply {
            this?.isEnabled = mainViewModel.liveEnableNext.value!!
            this?.isVisible = mainViewModel.liveShowNext.value!!
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.next -> {
                val action = mainViewModel.createActionForScanPage(
                    receiveViewModel,
                    topupViewModel
                )
                findChildController().navigate(action)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeToLiveData() {
        binding.setLifecycleOwner(this)
        binding.viewModel = mainViewModel

        with(mainViewModel) {
            observeEventFor(liveDestinationId) { destinationId ->
                findChildController().navigate(destinationId)
            }
            observeFor(liveShowFullScreen) {
                showFullscreen(it)
            }
            observeFor(liveEnableNext) {
                menuNext?.isEnabled = it
            }
            observeFor(liveShowNext) {
                menuNext?.isVisible = it
            }
            observeFor(liveFeedback) { feedback ->
                findChildController().navigateUp()
                findChildController().navigate(mainViewModel.createActionForFeedbackPage(feedback))
            }
        }

        observeFor(addressViewModel.liveAddress) {
            findChildController().navigateUp()
            findChildController().navigate(mainViewModel.createActionForConfirmPage(receiveViewModel, topupViewModel))
            addressViewModel.removeCache(it)
        }
    }

    private fun setupNavigationUI() {
        bottomNavigation.setupWithNavController(findChildController())
        toolbar.setupWithNavController(findChildController())
    }

    private fun setupToolbar() {
        val hostActivity = activity as AppCompatActivity
        hostActivity.setSupportActionBar(toolbar)
    }

    private fun showFullscreen(show: Int?) {
        bottomNavigation.visibility = show ?: View.VISIBLE
        toolbar.visibility = show ?: View.VISIBLE
    }
}
