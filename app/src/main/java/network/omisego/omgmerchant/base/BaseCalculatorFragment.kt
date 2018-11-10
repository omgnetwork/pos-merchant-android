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
import network.omisego.omgmerchant.behavior.BehaviorCalculatorController
import network.omisego.omgmerchant.extensions.findMainFragment
import network.omisego.omgmerchant.extensions.observeFor
import network.omisego.omgmerchant.extensions.provideMainFragmentViewModel
import network.omisego.omgmerchant.extensions.setError
import network.omisego.omgmerchant.pages.authorized.main.MainViewModel

abstract class BaseCalculatorFragment : BaseFragment() {
    lateinit var behavior: BehaviorCalculatorController
    lateinit var mainViewModel: MainViewModel

    abstract fun onSetTokens(tokens: PaginationList<Token>)

    abstract fun onSetSelectedToken(token: Token?)

    override fun onProvideViewModel() {
        mainViewModel = provideMainFragmentViewModel()
    }

    fun setupBehavior(behavior: BehaviorCalculatorController) {
        this.behavior = behavior
    }

    override fun onObserveLiveData() {
        findMainFragment().observeFor(behavior.liveCalculator) {
            notifyCalculatorStateChange()
        }

        findMainFragment().observeFor(behavior.liveSelectedToken) {
            notifyCalculatorStateChange()
        }

        with(mainViewModel) {
            observeFor(liveTokenAPIResult) {
                it.handle(
                    ::onSetTokens,
                    spinner::setError
                )
                onSetSelectedToken(behavior.liveSelectedToken.value)
            }
        }
    }

    private fun notifyCalculatorStateChange() {
        mainViewModel.liveEnableNext.value = behavior.shouldEnableNextButton()
        behavior.dispatchHelperTextState()
    }
}
