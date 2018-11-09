package network.omisego.omgmerchant.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 7/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.arch.lifecycle.MutableLiveData
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.pagination.PaginationList
import com.jaredrummler.materialspinner.MaterialSpinner
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter
import network.omisego.omgmerchant.R

var MaterialSpinner.selectedToken: Token?
    get() = getItems<Token>()[this.selectedIndex]
    set(value) {
        val tokens = getItems<Token>()
        if (tokens.size > 1) {
            val selectedTokenId = value?.id ?: tokens[0].id
            selectedIndex = tokens.indexOfFirst { it.id == selectedTokenId }
        }
    }

fun MaterialSpinner.setTokens(tokens: PaginationList<Token>, liveSelectedToken: MutableLiveData<Token>? = null) {
    liveSelectedToken?.let {
        setOnItemSelectedListener { _, position, _, _ ->
            liveSelectedToken.value = getItems<Token>()[position]
        }
    }

    if (tokens.data.isNotEmpty()) {
        setItems(tokens.data)
        setAdapter(defaultAdapter(tokens))
        selectedIndex = getItems<Token>().indexOfFirst { it.id == liveSelectedToken?.value?.id ?: selectedToken?.id }
        liveSelectedToken?.value = selectedToken
    } else {
        val emptyMsg = this.context?.getString(R.string.token_spinner_empty_list)
        setItems(emptyMsg)
    }
}

fun MaterialSpinner.setError(error: APIError) {
    val errorMsg = this.context?.getString(R.string.token_spinner_fetch_error)
    setItems(errorMsg ?: error.description)
    this.error
}

fun MaterialSpinner.defaultAdapter(tokens: PaginationList<Token>): MaterialSpinnerAdapter<Any> = object : MaterialSpinnerAdapter<Any>(this.context, tokens.data) {
    override fun get(position: Int) = (items[position] as Token).symbol.toUpperCase()
    override fun getItem(position: Int): Any {
        return if (isHintEnabled) {
            (items[position] as Token).symbol.toUpperCase()
        } else if (position >= selectedIndex && items.size != 1) {
            (items[position + 1] as Token).symbol.toUpperCase()
        } else {
            (items[position] as Token).symbol.toUpperCase()
        }
    }
}
