package network.omisego.omgmerchant.pages.authorized.main.more.account

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Account
import co.omisego.omisego.model.pagination.PaginationList
import kotlinx.android.synthetic.main.fragment_setting_account.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.BaseFragment
import network.omisego.omgmerchant.custom.CustomLoadingRecyclerAdapter
import network.omisego.omgmerchant.custom.CustomRecyclerMarginDivider
import network.omisego.omgmerchant.databinding.FragmentSettingAccountBinding
import network.omisego.omgmerchant.databinding.ViewholderSettingAccountBinding
import network.omisego.omgmerchant.extensions.findChildController
import network.omisego.omgmerchant.extensions.observeEventFor
import network.omisego.omgmerchant.extensions.observeFor
import network.omisego.omgmerchant.extensions.provideMarginLeft
import network.omisego.omgmerchant.extensions.provideViewModel
import network.omisego.omgmerchant.extensions.toast

class SettingAccountFragment : BaseFragment() {
    private lateinit var binding: FragmentSettingAccountBinding
    private lateinit var viewModel: SettingAccountViewModel
    private lateinit var adapter: CustomLoadingRecyclerAdapter<Account, ViewholderSettingAccountBinding>
    private var menuSave: MenuItem? = null

    override fun onProvideViewModel() {
        viewModel = provideViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_setting_account,
            container,
            false
        )
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_account, menu)
        menuSave = menu?.findItem(R.id.save)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.save -> {
                val account = viewModel.saveAccount()
                toast("Change account to ${account.name}")
                findChildController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onObserveLiveData() {
        with(viewModel) {
            observeEventFor(liveAccountList) {
                it.handle(
                    this@SettingAccountFragment::handleLoadAccount,
                    this@SettingAccountFragment::handleLoadAccountFail
                )
            }
            observeFor(liveAccountSelect) {
                menuSave?.isEnabled = viewModel.loadSelectedAccount().id != it?.id
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dividerDecorator = CustomRecyclerMarginDivider(context!!, context!!.provideMarginLeft(80))

        adapter = CustomLoadingRecyclerAdapter(R.layout.viewholder_account_loading, R.layout.viewholder_setting_account, viewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(dividerDecorator)

        adapter.addLoadingItems(10)
        viewModel.loadAccounts()
    }

    private fun handleLoadAccount(account: PaginationList<Account>) {
        adapter.addItems(account.data)
    }

    private fun handleLoadAccountFail(error: APIError) {
        toast(error.description)
    }
}
