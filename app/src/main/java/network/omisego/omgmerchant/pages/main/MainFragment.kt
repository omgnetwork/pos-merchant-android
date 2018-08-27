package network.omisego.omgmerchant.pages.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_main.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.extensions.getDrawableCompat
import network.omisego.omgmerchant.extensions.provideActivityAndroidViewModel
import network.omisego.omgmerchant.extensions.provideActivityViewModel
import network.omisego.omgmerchant.extensions.replaceFragment
import network.omisego.omgmerchant.pages.main.more.MoreFragment
import network.omisego.omgmerchant.pages.main.more.setting.SettingViewModel
import network.omisego.omgmerchant.pages.main.receive.ReceiveFragment
import network.omisego.omgmerchant.pages.main.receive.ReceiveViewModel
import network.omisego.omgmerchant.pages.main.topup.TopupFragment
import network.omisego.omgmerchant.pages.main.topup.TopupViewModel

class MainFragment : Fragment() {

    /* ViewModel */
    private lateinit var receiveViewModel: ReceiveViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var topupViewModel: TopupViewModel
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var toolbarViewModel: ToolbarViewModel

    /* Local */
    private var showSplash = true
    private var menuNext: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = provideActivityViewModel()
        receiveViewModel = provideActivityViewModel()
        topupViewModel = provideActivityViewModel()
        settingViewModel = provideActivityAndroidViewModel()
        toolbarViewModel = provideActivityAndroidViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)
        menuNext = menu?.findItem(R.id.next).apply { this?.isEnabled = mainViewModel.liveEnableNext.value!! }
        mainViewModel.liveEnableNext.observe(this, Observer {
            menuNext?.isEnabled = it ?: false
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.next -> {
                val action = mainViewModel.createActionForScanPage(
                    receiveViewModel,
                    topupViewModel
                )
                NavHostFragment.findNavController(this).navigate(action)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupConditionalNavigationGraph()
    }

    private fun initView() {
        setupToolbar()
        listenBottomNavSelected()
        subscribePageChanged()
    }

    private fun setupConditionalNavigationGraph() {
        if (!mainViewModel.hasCredential()) {
            NavHostFragment.findNavController(this).navigate(R.id.action_global_sign_in)
        } else if (mainViewModel.getAccount() == null) {
            NavHostFragment.findNavController(this).navigate(R.id.action_main_to_selectAccount)
        } else if (mainViewModel.getFeedback() != null) {
            NavHostFragment.findNavController(this).navigate(mainViewModel.createActionForFeedbackPage())
        } else if (showSplash) {
            NavHostFragment.findNavController(this).navigate(R.id.action_main_to_splash)
            showSplash = false
            mainViewModel.loadWalletAndSave()
        }

        /* Observe sign */
        settingViewModel.liveSignOut.observe(this, Observer {
            it?.let { isSignOut ->
                if (isSignOut) {
                    NavHostFragment.findNavController(this).navigate(R.id.action_global_sign_in)
                }
            }
        })
    }

    private fun subscribePageChanged() {
        /* Return if already subscribed */
        if (mainViewModel.livePage.hasObservers()) return

        mainViewModel.livePage.observe(this, Observer {
            menuNext?.isVisible = true
            when (it!!) {
                PAGE_RECEIVE -> {
                    replaceFragment(fragment = ReceiveFragment())
                    settingViewModel.setLiveMenu(null)
                    mainViewModel.handleEnableNextButtonByPager(receiveViewModel.liveCalculator, PAGE_RECEIVE)
                    toolbarViewModel.setToolbarTitle(getString(R.string.receive_title))
                }
                PAGE_TOPUP -> {
                    replaceFragment(fragment = TopupFragment())
                    settingViewModel.setLiveMenu(null)
                    mainViewModel.handleEnableNextButtonByPager(topupViewModel.liveCalculator, PAGE_TOPUP)
                    toolbarViewModel.setToolbarTitle(getString(R.string.topup_title))
                }
                PAGE_MORE -> {
                    replaceFragment(fragment = MoreFragment())
                    toolbarViewModel.setToolbarTitle(getString(R.string.more_title))
                    menuNext?.isVisible = false
                }
            }
        })
    }

    private fun listenBottomNavSelected() {
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_receive -> mainViewModel.movePage(PAGE_RECEIVE)
                R.id.action_topup -> mainViewModel.movePage(PAGE_TOPUP)
                R.id.action_more -> mainViewModel.movePage(PAGE_MORE)
                else -> throw IllegalStateException("Unsupported operation")
            }
            true
        }
    }

    private fun setupToolbar() {
        val hostActivity = activity as AppCompatActivity
        hostActivity.setSupportActionBar(toolbar)
        settingViewModel.getLiveMenu().observe(this, Observer { it ->
            if (it == null) {
                toolbar.navigationIcon = null
            } else {
                toolbar.navigationIcon = context?.getDrawableCompat(R.drawable.ic_arrow_back)
            }
        })
        toolbarViewModel.getLiveToolbarTitle().observe(this, Observer {
            toolbar.title = it
        })
        toolbar.setNavigationOnClickListener { settingViewModel.setLiveMenu(null) }
    }
}
