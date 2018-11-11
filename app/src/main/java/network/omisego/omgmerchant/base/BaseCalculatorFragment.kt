package network.omisego.omgmerchant.base

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 10/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.model.Token
import co.omisego.omisego.model.pagination.PaginationList
import kotlinx.android.synthetic.main.fragment_topup.*
import network.omisego.omgmerchant.pages.authorized.main.AbstractCalculatorController
import network.omisego.omgmerchant.extensions.findMainFragment
import network.omisego.omgmerchant.extensions.observeFor
import network.omisego.omgmerchant.extensions.provideMainFragmentViewModel
import network.omisego.omgmerchant.extensions.setError
import network.omisego.omgmerchant.pages.authorized.main.MainViewModel

abstract class BaseCalculatorFragment : BaseFragment() {
    lateinit var controller: AbstractCalculatorController
    lateinit var mainViewModel: MainViewModel

    abstract fun onSetTokens(tokens: PaginationList<Token>)

    abstract fun onSetSelectedToken(token: Token?)

    override fun onProvideViewModel() {
        mainViewModel = provideMainFragmentViewModel()
    }

    fun setupBehavior(controller: AbstractCalculatorController) {
        this.controller = controller
    }

    override fun onObserveLiveData() {
        findMainFragment().observeFor(controller.liveCalculator) {
            notifyCalculatorStateChange()
        }

        findMainFragment().observeFor(controller.liveSelectedToken) {
            notifyCalculatorStateChange()
        }

        with(mainViewModel) {
            observeFor(liveTokenAPIResult) {
                it.handle(
                    ::onSetTokens,
                    spinner::setError
                )
                onSetSelectedToken(controller.liveSelectedToken.value)
            }
        }
    }

    private fun notifyCalculatorStateChange() {
        mainViewModel.liveEnableNext.value = controller.shouldEnableNextButton()
        controller.dispatchHelperTextState()
    }
}
