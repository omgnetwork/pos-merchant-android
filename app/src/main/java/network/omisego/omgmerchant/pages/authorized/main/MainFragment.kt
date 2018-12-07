package network.omisego.omgmerchant.pages.authorized.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.ui.setupWithNavController
import co.omisego.omisego.constant.enums.ErrorCode
import co.omisego.omisego.model.APIError
import kotlinx.android.synthetic.main.fragment_main.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.BaseFragment
import network.omisego.omgmerchant.databinding.FragmentMainBinding
import network.omisego.omgmerchant.extensions.findChildController
import network.omisego.omgmerchant.extensions.findRootController
import network.omisego.omgmerchant.extensions.logi
import network.omisego.omgmerchant.extensions.observeEventFor
import network.omisego.omgmerchant.extensions.observeFor
import network.omisego.omgmerchant.extensions.provideMainFragmentAndroidViewModel
import network.omisego.omgmerchant.extensions.provideMainFragmentViewModel
import network.omisego.omgmerchant.livedata.Event
import network.omisego.omgmerchant.pages.authorized.confirm.ConfirmViewModel
import network.omisego.omgmerchant.pages.authorized.loading.LoadingViewModel
import network.omisego.omgmerchant.pages.authorized.main.receive.ReceiveViewModel
import network.omisego.omgmerchant.pages.authorized.main.topup.TopupViewModel
import network.omisego.omgmerchant.pages.authorized.scan.ScanViewModel

class MainFragment : BaseFragment() {

    /* ViewModel */
    private lateinit var receiveViewModel: ReceiveViewModel
    private lateinit var topupViewModel: TopupViewModel
    private lateinit var confirmViewModel: ConfirmViewModel
    private lateinit var scanViewModel: ScanViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var loadingViewModel: LoadingViewModel

    /* Local */
    private lateinit var binding: FragmentMainBinding
    private var menuNext: MenuItem? = null

    override fun onProvideViewModel() {
        mainViewModel = provideMainFragmentViewModel()
        confirmViewModel = provideMainFragmentAndroidViewModel()
        receiveViewModel = provideMainFragmentAndroidViewModel()
        topupViewModel = provideMainFragmentAndroidViewModel()
        scanViewModel = provideMainFragmentAndroidViewModel()
        loadingViewModel = provideMainFragmentViewModel()
    }

    override fun onReceiveArgs() {
        confirmViewModel.liveDirection = mainViewModel.liveDirection
        scanViewModel.liveDirection = mainViewModel.liveDirection
        loadingViewModel.liveDirection = mainViewModel.liveDirection
        loadingViewModel.liveCancelTransactionConsumption = mainViewModel.liveCancelTransactionConsumption
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
            observeFor(liveToolbarBottomNavVisibility) {
                showFullscreen(it)
            }
            observeFor(liveEnableNext) {
                menuNext?.isEnabled = it
            }
            observeFor(liveShowNext) {
                menuNext?.isVisible = it
            }

            observeEventFor(liveDirection) { direction ->
                logi("Current dest: ${findChildController().currentDestination?.label}, Next dest: ${direction::class.java.simpleName}")
                if (findChildController().currentDestination?.id !in arrayOf(R.id.receive, R.id.topup)) {
                    findChildController().navigateUp()
                }
                findChildController().navigate(direction)
            }

            observeFor(liveTokenAPIResult) {
                it.handle(mainViewModel::handleLoadTokenSuccess, object : (APIError) -> Unit {
                    override fun invoke(error: APIError) {
                        if (error.code == ErrorCode.CLIENT_INVALID_AUTH_SCHEME) {
                            clearSession()
                            findRootController().popBackStack()
                            findRootController().navigate(R.id.action_global_sign_in)
                        }
                    }
                })

                observeFor(liveCancelTransactionConsumption) { cancelClick ->
                    if (cancelClick) {
                        confirmViewModel.handler?.stopListening()
                    }
                }
            }
        }

        with(confirmViewModel) {
            observeEventFor(liveYesClick) {
                handleQRPayload()
            }
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
            this?.isEnabled = mainViewModel.liveEnableNext.value ?: false
            this?.isVisible = mainViewModel.liveShowNext.value ?: false
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.next -> {
                val (amount, token) = mainViewModel.getAmountTokenPairByCalculatorMode(
                    receiveViewModel,
                    topupViewModel
                )
                val action = mainViewModel.createDestinationQRScan(
                    mainViewModel.currentCalculatorMode,
                    amount,
                    token
                )
                mainViewModel.liveDirection.value = Event(action)
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
