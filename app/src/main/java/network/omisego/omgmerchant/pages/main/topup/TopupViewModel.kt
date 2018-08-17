package network.omisego.omgmerchant.pages.main.topup

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import co.omisego.omisego.model.Token
import network.omisego.omgmerchant.calculator.CalculatorHandler
import network.omisego.omgmerchant.model.LiveCalculator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 9/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class TopupViewModel(
    val handler: CalculatorHandler,
    val liveCalculator: LiveCalculator
) : ViewModel(), CalculatorHandler.Operation {
    val liveToken: MutableLiveData<Token> by lazy { MutableLiveData<Token>() }

    override fun onAppend(char: CharSequence) {
        if (liveCalculator.value?.contains(".") == true && char == ".") return
        if (liveCalculator.value == "0") liveCalculator.value = ""
        liveCalculator.value += char
    }

    override fun onDelete() {
        liveCalculator.value = liveCalculator.value?.dropLast(1)
        if (liveCalculator.value?.isEmpty() == true) {
            liveCalculator.value = "0"
        }
    }

    override fun onClear() {
        // Clear isn't available on the topup page.
    }

    // Evaluate isn't available on the topup page.
    override fun onEvaluate(): Boolean = false

    init {
        handler.operation = this
    }
}