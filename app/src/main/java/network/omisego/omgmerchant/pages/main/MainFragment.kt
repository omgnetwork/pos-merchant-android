package network.omisego.omgmerchant.pages.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.model.pagination.PaginationList
import kotlinx.android.synthetic.main.fragment_main.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.extensions.get
import network.omisego.omgmerchant.extensions.getDrawableCompat
import network.omisego.omgmerchant.extensions.logi
import network.omisego.omgmerchant.extensions.provideActivityViewModel
import network.omisego.omgmerchant.extensions.toast
import network.omisego.omgmerchant.pages.main.receive.ReceiveViewModel
import network.omisego.omgmerchant.pages.main.topup.TopupViewModel
import network.omisego.omgmerchant.storage.Storage

class MainFragment : Fragment() {
    private lateinit var receiveViewModel: ReceiveViewModel
    private lateinit var pagerAdapter: MainPagerAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var topupViewModel: TopupViewModel
    private var showSplash = true
    private var menuNext: MenuItem? = null
    private var currentPage: Int = 0
    private val credential
        get() = Storage.loadCredential()
    private val account
        get() = Storage.loadAccount()
    private val feedback
        get() = Storage.loadFeedback()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = provideActivityViewModel()
        receiveViewModel = provideActivityViewModel()
        topupViewModel = provideActivityViewModel()
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
                val bundle = Bundle().apply {
                    val amount = if (currentPage == 0) {
                        this.putString("transaction_type", "receive")
                        receiveViewModel.liveCalculator.value
                    } else {
                        this.putString("transaction_type", "topup")
                        topupViewModel.liveCalculator.value
                    }
                    val token = if (currentPage == 0) {
                        receiveViewModel.liveToken.value
                    } else {
                        topupViewModel.liveToken.value
                    }
                    this.putDouble("amount", amount?.toDouble()!!)
                    this.putParcelable("token", token!!)

                }
                NavHostFragment.findNavController(this).navigate(R.id.action_main_to_scanFragment, bundle)
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

    private fun setupConditionalNavigationGraph() {
        val (userId, authenticationToken) = credential
        if (userId.isEmpty() || authenticationToken.isEmpty()) {
            NavHostFragment.findNavController(this).navigate(R.id.action_global_sign_in)
        } else if (account == null) {
            NavHostFragment.findNavController(this).navigate(R.id.action_main_to_selectAccount)
        } else if (feedback != null) {
            val bundle = Bundle().apply { this.putParcelable("feedback", feedback!!) }
            NavHostFragment.findNavController(this).navigate(R.id.action_main_to_feedback, bundle)
        } else if (showSplash) {
            NavHostFragment.findNavController(this).navigate(R.id.action_main_to_splash)
            showSplash = false
            mainViewModel.getWallet().observe(this, Observer {
                it?.handle(
                    this::handleLoadWalletSuccess,
                    this::handleLoadWalletFail
                )
            })
        }
    }

    private fun handleLoadWalletSuccess(listWallet: PaginationList<Wallet>) {
        logi(listWallet)
        Storage.saveWallet(listWallet.data.findLast { it.name == "primary" }!!)
    }

    private fun handleLoadWalletFail(error: APIError) {
        logi(error)
        toast(error.description)
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
        changeToolbarTitleWhenPageChanged()
    }

    private fun changeToolbarTitleWhenPageChanged() {
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                currentPage = position
                val toolbarTitle = when (position) {
                    0 -> {
                        val receiveCalculatorValue = receiveViewModel.liveCalculator.value
                        mainViewModel.liveEnableNext.value = receiveCalculatorValue != "0" && receiveCalculatorValue?.indexOfAny(charArrayOf('-', '+')) == -1
                        "Receive"
                    }
                    1 -> {
                        val topupCalculatorValue = topupViewModel.liveCalculator.value
                        mainViewModel.liveEnableNext.value = topupCalculatorValue != "0"
                        "Topup"
                    }
                    2 -> {
                        mainViewModel.liveEnableNext.value = false
                        "More"
                    }
                    else -> throw IllegalStateException("Unsupported operation")
                }
                toolbar.title = toolbarTitle
            }
        })
    }

    private fun setupToolbar() {
        val hostActivity = activity as AppCompatActivity
        hostActivity.setSupportActionBar(toolbar)
        toolbar.title = "Receive"
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
