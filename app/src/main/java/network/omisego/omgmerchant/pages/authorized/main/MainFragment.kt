package network.omisego.omgmerchant.pages.authorized.main

import android.databinding.DataBindingUtil
import android.os.Bundle
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
import network.omisego.omgmerchant.base.BaseFragment
import network.omisego.omgmerchant.databinding.FragmentMainBinding
import network.omisego.omgmerchant.extensions.findChildController
import network.omisego.omgmerchant.extensions.observeEventFor
import network.omisego.omgmerchant.extensions.observeFor
import network.omisego.omgmerchant.extensions.provideMainFragmentAndroidViewModel
import network.omisego.omgmerchant.extensions.provideMainFragmentViewModel
import network.omisego.omgmerchant.pages.authorized.confirm.ConfirmViewModel
import network.omisego.omgmerchant.pages.authorized.confirm.handler.QRHandlerManager
import network.omisego.omgmerchant.pages.authorized.main.receive.ReceiveViewModel
import network.omisego.omgmerchant.pages.authorized.main.topup.TopupViewModel
import network.omisego.omgmerchant.pages.authorized.scan.QRPayloadViewModel

class MainFragment : BaseFragment() {

    /* ViewModel */
    private lateinit var receiveViewModel: ReceiveViewModel
    private lateinit var topupViewModel: TopupViewModel
    private lateinit var confirmViewModel: ConfirmViewModel
    private lateinit var qrPayloadViewModel: QRPayloadViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var qrHandlerManager: QRHandlerManager

    /* Local */
    private lateinit var binding: FragmentMainBinding
    private var menuNext: MenuItem? = null

    override fun onProvideViewModel() {
        qrPayloadViewModel = provideMainFragmentViewModel()
        mainViewModel = provideMainFragmentViewModel()
        confirmViewModel = provideMainFragmentAndroidViewModel()
        receiveViewModel = provideMainFragmentViewModel()
        topupViewModel = provideMainFragmentViewModel()

        qrHandlerManager = QRHandlerManager(this).apply {
            this.liveAPIError = mainViewModel.liveError
            this.liveLoading = mainViewModel.liveLoading
            this.liveFeedback = mainViewModel.liveFeedback
        }
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
        mainViewModel.getTokens()
        mainViewModel.displayOtherDestinationByCondition()
    }

    override fun onBindDataBinding() {
        binding.setLifecycleOwner(this)
        binding.viewModel = mainViewModel
    }

    override fun onObserveLiveData() {
        with(mainViewModel) {
            observeEventFor(liveDestinationId) { destinationId ->
                findChildController().navigate(destinationId)
            }
            observeFor(liveToolbarBottomNavVisibility) {
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
            observeEventFor(liveLoading) { loading ->
                if (loading) {
                    findChildController().navigate(R.id.action_global_loadingFragment)
                }
            }
        }

        with(confirmViewModel) {
            observeEventFor(liveYesClick) {
                findChildController().navigateUp()
                qrHandlerManager.handleQRPayload(confirmViewModel.qrPayload, args)
            }
        }

        observeFor(qrPayloadViewModel.liveQRPayload) {
            findChildController().navigateUp()
            findChildController().navigate(mainViewModel.createActionForConfirmPage(receiveViewModel, topupViewModel))
            qrPayloadViewModel.removeCache(it)
        }
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
