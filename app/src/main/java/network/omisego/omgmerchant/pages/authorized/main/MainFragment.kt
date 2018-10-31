package network.omisego.omgmerchant.pages.authorized.main

import android.arch.lifecycle.Observer
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
import network.omisego.omgmerchant.extensions.provideActivityViewModel
import network.omisego.omgmerchant.extensions.provideMainFragmentViewModel
import network.omisego.omgmerchant.livedata.EventObserver
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
        addressViewModel = provideMainFragmentViewModel()
        mainViewModel = provideActivityViewModel()
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

    private fun subscribeToLiveData() {
        binding.setLifecycleOwner(this)
        binding.viewModel = mainViewModel

        mainViewModel.liveDestinationId.observe(this, EventObserver { destinationId ->
            findChildController().navigate(destinationId)
        })

        mainViewModel.liveEnableNext.observe(this, Observer {
            menuNext?.isEnabled = it ?: false
        })

        mainViewModel.liveShowNext.observe(this, EventObserver {
            menuNext?.isVisible = it
        })
    }

    private fun setupNavigationUI() {
        bottomNavigation.setupWithNavController(findChildController())
        toolbar.setupWithNavController(findChildController())
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)

        /* Restore the next button state */
        menuNext = menu?.findItem(R.id.next).apply {
            this?.isEnabled = mainViewModel.liveEnableNext.value!!
            this?.isVisible = mainViewModel.liveShowNext.value?.peekContent()!!
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupToolbar() {
        val hostActivity = activity as AppCompatActivity
        hostActivity.setSupportActionBar(toolbar)
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

//    private fun setupConditionalNavigationGraph() {
//        if (!mainViewModel.hasCredential()) {
//            NavHostFragment.findNavController(this).navigate(R.id.action_global_sign_in)
//        } else if (mainViewModel.getAccount() == null) {
//            NavHostFragment.findNavController(this).navigate(R.id.action_main_to_selectAccount)
//        } else if (addressViewModel.liveAddress.value != null) {
//            NavHostFragment.findNavController(this).navigate(mainViewModel.createActionForConfirmPage(receiveViewModel, topupViewModel))
//        } else if (mainViewModel.getFeedback() != null) {
//            NavHostFragment.findNavController(this).navigate(mainViewModel.createActionForFeedbackPage())
//        } else if (showSplash) {
//            NavHostFragment.findNavController(this).navigate(R.id.action_main_to_splash)
//            showSplash = false
//            mainViewModel.loadWalletAndSave()
//        }
//
//    }
}
