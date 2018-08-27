package network.omisego.omgmerchant.pages.main.receive

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.Token
import network.omisego.omgmerchant.calculator.Calculation
import network.omisego.omgmerchant.calculator.CalculatorInteraction
import network.omisego.omgmerchant.model.LiveCalculator
import network.omisego.omgmerchant.pages.main.shared.spinner.LiveTokenSpinner
import network.omisego.omgmerchant.pages.main.shared.spinner.TokenSpinnerViewModel

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 7/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class ReceiveViewModel(
    val handler: CalculatorInteraction,
    override val liveCalculator: LiveCalculator,
    private val calculation: Calculation
) : ViewModel(), CalculatorInteraction.Operation, TokenSpinnerViewModel {
    override val liveToken: MutableLiveData<Token> by lazy { MutableLiveData<Token>() }
    var liveTokenSpinner: LiveTokenSpinner? = null

    /* Implement CalculatorInteraction.Operation */
    override fun onAppend(char: CharSequence) {
        if (liveCalculator.value?.contains(".") == true && char == ".") return
        if (liveCalculator.value == "0") liveCalculator.value = ""
        liveCalculator.value += char
    }

    override fun onClear() {
        liveCalculator.value = "0"
    }

    override fun onDelete() {
        // Delete isn't available on the receive page.
    }

    override fun onEvaluate(): Boolean {
        val evaluated = calculation.evaluate(liveCalculator.value!!)
        if (evaluated == liveCalculator.value) return false
        liveCalculator.value = evaluated
        return true
    }

    override fun startListeningTokenSpinner() {
        liveTokenSpinner?.listen()
        liveTokenSpinner?.start()
    }

    override fun onCleared() {
        liveTokenSpinner?.stop()
    }

    init {
        handler.operation = this
    }
}
