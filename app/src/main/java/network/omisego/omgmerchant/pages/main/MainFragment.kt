package network.omisego.omgmerchant.pages.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
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
import network.omisego.omgmerchant.extensions.get
import network.omisego.omgmerchant.extensions.getDrawableCompat
import network.omisego.omgmerchant.extensions.provideActivityAndroidViewModel
import network.omisego.omgmerchant.extensions.provideActivityViewModel
import network.omisego.omgmerchant.pages.main.more.setting.SettingViewModel
import network.omisego.omgmerchant.pages.main.receive.ReceiveViewModel
import network.omisego.omgmerchant.pages.main.topup.TopupViewModel
import network.omisego.omgmerchant.utils.MinimalPageChangeListener

class MainFragment : Fragment() {

    /* ViewModel */
    private lateinit var receiveViewModel: ReceiveViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var topupViewModel: TopupViewModel
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var toolbarViewModel: ToolbarViewModel

    /* Adapter */
    private lateinit var pagerAdapter: MainPagerAdapter

    /* Local */
    private var showSplash = true
    private var menuNext: MenuItem? = null
    private var currentPage: Int = 0

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
                    topupViewModel,
                    currentPage
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
        pagerAdapter = MainPagerAdapter(childFragmentManager)
        viewpager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewpager)
        with(tabLayout) {
            getTabAt(0)?.icon = tabLayout.context.getDrawableCompat(R.drawable.ic_tab_wallet)
            getTabAt(1)?.icon = tabLayout.context.getDrawableCompat(R.drawable.ic_tab_topup)
            getTabAt(2)?.icon = tabLayout.context.getDrawableCompat(R.drawable.ic_tab_more)
        }
        tintTabLayoutIcon()
        listenPageChanged()

        /* A bit hacky way to select the first tab as a default tab.*/
        tabLayout[2]?.select()
        tabLayout[0]?.select()
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

    private fun listenPageChanged() {
        viewpager.addOnPageChangeListener(MinimalPageChangeListener { position ->
            currentPage = position
            menuNext?.isVisible = true
            val toolbarTitle = when (position) {
                PAGE_RECEIVE -> {
                    settingViewModel.liveMenu.value = null
                    mainViewModel.handleEnableNextButtonByPager(receiveViewModel.liveCalculator, PAGE_RECEIVE)
                    getString(R.string.receive_title)
                }
                PAGE_TOPUP -> {
                    settingViewModel.liveMenu.value = null
                    mainViewModel.handleEnableNextButtonByPager(topupViewModel.liveCalculator, PAGE_TOPUP)
                    getString(R.string.topup_title)
                }
                PAGE_MORE -> {
                    /* liveCalculator isn't necessary in this case */
                    mainViewModel.handleEnableNextButtonByPager(topupViewModel.liveCalculator, PAGE_MORE)
                    menuNext?.isVisible = false
                    getString(R.string.more_title)
                }
                else -> throw IllegalStateException("Unsupported operation")
            }
            toolbarViewModel.liveToolbarText.value = toolbarTitle
        })
    }

    private fun setupToolbar() {
        val hostActivity = activity as AppCompatActivity
        hostActivity.setSupportActionBar(toolbar)
        settingViewModel.liveMenu.observe(this, Observer { it ->
            if (it == null) {
                toolbar.navigationIcon = null
            } else {
                toolbar.navigationIcon = context?.getDrawableCompat(R.drawable.ic_arrow_back)
            }
        })
        toolbarViewModel.liveToolbarText.observe(this, Observer {
            toolbar.title = it
        })
        toolbarViewModel.liveToolbarText.value = getString(R.string.receive_title)
        toolbar.setNavigationOnClickListener { settingViewModel.liveMenu.value = null }
    }

    private fun tintTabLayoutIcon() {
        for (i in 0 until tabLayout.tabCount) {
            with(tabLayout[i]?.icon) {
                this?.let {
                    DrawableCompat.setTintList(
                        DrawableCompat.wrap(it),
                        ContextCompat.getColorStateList(tabLayout.context, R.color.color_gray_blue)
                    )
                }
            }
        }
    }
}
