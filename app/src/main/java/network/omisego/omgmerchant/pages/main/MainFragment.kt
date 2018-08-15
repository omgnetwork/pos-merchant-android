package network.omisego.omgmerchant.pages.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_main.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.extensions.get
import network.omisego.omgmerchant.extensions.getDrawableCompat
import network.omisego.omgmerchant.extensions.logi
import network.omisego.omgmerchant.storage.Storage

class MainFragment : Fragment() {
    private var showSplash = true
    private lateinit var pagerAdapter: MainPagerAdapter
    private val credential
        get() = Storage.loadCredential()
    private val account
        get() = Storage.loadAccount()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupConditionalNavigationGraph()
    }

    private fun setupConditionalNavigationGraph() {
        val (userId, authenticationToken) = credential
        if (userId.isEmpty() || authenticationToken.isEmpty()) {
            findNavController().navigate(R.id.action_global_sign_in)
        } else if (account == null) {
            findNavController().navigate(R.id.action_main_to_selectAccount)
        } else if (showSplash) {
            findNavController().navigate(R.id.action_main_to_splash)
            showSplash = false
        }
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
                val toolbarTitle = when (position) {
                    0 -> "Receive"
                    1 -> "Topup"
                    2 -> "More"
                    else -> throw IllegalStateException("Unsupported operation")
                }
                toolbar.title = toolbarTitle
                logi(position)
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
