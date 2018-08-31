package network.omisego.omgmerchant.pages.main.more.settinghelp

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.AuthenticationToken
import kotlinx.android.synthetic.main.bottom_sheet_enter_password.*
import kotlinx.android.synthetic.main.bottom_sheet_enter_password.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.extensions.logd
import network.omisego.omgmerchant.extensions.provideViewModel
import network.omisego.omgmerchant.extensions.toast

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class ConfirmFingerprintDialog : BottomSheetDialogFragment() {
    private lateinit var viewModel: ConfirmFingerprintViewModel
    private var liveConfirmSuccess: MutableLiveData<Boolean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        liveConfirmSuccess?.value = false
    }

    fun setLiveConfirmSuccess(liveConfirmSuccess: MutableLiveData<Boolean>) {
        this.liveConfirmSuccess = liveConfirmSuccess
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.bottom_sheet_enter_password, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.btnConfirm.setOnClickListener {
            btnConfirm.isEnabled = false
            viewModel.signIn(view.etPassword.text.toString())
        }

        viewModel.liveAPIResult.observe(this, Observer {
            it?.handle(this::handleSignInSuccess, this::handleSignInError)
        })
    }

    private fun handleSignInError(error: APIError) {
        logd(error)
        toast(error.description)
        liveConfirmSuccess?.value = false
        btnConfirm.isEnabled = true
    }

    private fun handleSignInSuccess(data: AuthenticationToken) {
        launch(UI) {
            viewModel.saveCredential(data).await()
            viewModel.saveUserPassword(etPassword.text.toString())
            liveConfirmSuccess?.value = true
            btnConfirm.isEnabled = true
        }
    }
}
