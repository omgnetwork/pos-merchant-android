package network.omisego.omgmerchant.pages.authorized.feedback

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 17/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.graphics.drawable.Drawable
import network.omisego.omgmerchant.helper.map
import network.omisego.omgmerchant.model.Feedback

class FeedbackViewModel(
    val app: Application,
    private val transformer: FeedbackTransformer
) : AndroidViewModel(app) {
    val liveFeedback: MutableLiveData<Feedback> = MutableLiveData()

    /* binding data */
    val icon: LiveData<Drawable> = liveFeedback.map { transformer.transformIcon(app, it) }
    val iconText: LiveData<String> = liveFeedback.map { transformer.transformIconText(app, it) }
    val title: LiveData<String> = liveFeedback.map { transformer.transformTitle(app, it) }
    val amount: LiveData<String> = liveFeedback.map { transformer.transformAmount(app, it) }
    val userId: LiveData<String> = liveFeedback.map { transformer.transformUserId(app, it) }
    val userName: LiveData<String> = liveFeedback.map { transformer.transformUserName(app, it) }
    val date: LiveData<String> = liveFeedback.map { transformer.transformDate(app, it) }
    val errorCode: LiveData<String> = liveFeedback.map { transformer.transformErrorCode(app, it) }
    val errorDescription: LiveData<String> = liveFeedback.map { transformer.transformErrorDescription(app, it) }
    val liveShowError: LiveData<Boolean> = liveFeedback.map { it.error != null }
}
