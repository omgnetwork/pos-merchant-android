package network.omisego.omgmerchant.pages.main.shared.spinner

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 17/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.pagination.PaginationList
import com.jaredrummler.materialspinner.MaterialSpinner
import network.omisego.omgmerchant.model.APIResult
import network.omisego.omgmerchant.model.LiveCalculator

class LiveTokenSpinner(
    private var spinner: MaterialSpinner? = null,
    val viewModel: TokenSpinnerViewModel,
    private val loadTokenViewModel: LoadTokenViewModel,
    private val errorMsg: String? = null
) : LifecycleOwner {
    private val lifecycleRegistry: LifecycleRegistry by lazy { LifecycleRegistry(this) }
    private var tokenList: MutableList<Token> = mutableListOf()

    override fun getLifecycle() = lifecycleRegistry

    fun listen() {
        spinner?.setOnItemSelectedListener { _, position, _, _ ->
            viewModel.liveToken.value = tokenList[position]
        }
    }

    fun setTokens(tokens: PaginationList<Token>) {
        tokenList.addAll(tokens.data)
        viewModel.liveToken.value = viewModel.liveToken.value ?: tokenList[0]
        spinner?.setItems(tokens.data.map { it.symbol.toUpperCase() })
    }

    fun setError(error: APIError) {
        spinner?.setItems(errorMsg ?: error.description)
    }

    fun start() {
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
        loadTokenViewModel.liveTokenAPIResult.observe(this, Observer {
            it?.handle(this::setTokens, this::setError)
        })
    }

    fun stop() {
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
    }
}

interface TokenSpinnerViewModel {
    val liveCalculator: LiveCalculator
    val liveToken: MutableLiveData<Token>

    fun startListeningTokenSpinner()
}

interface LoadTokenViewModel {
    val liveTokenAPIResult: MutableLiveData<APIResult>
}
