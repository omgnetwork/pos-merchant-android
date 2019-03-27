package network.omisego.omgmerchant.pages.authorized.feedback

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 17/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import network.omisego.omgmerchant.extensions.map
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
    val id: LiveData<String> = liveFeedback.map { transformer.transformId(app, it) }
    val name: LiveData<String> = liveFeedback.map { transformer.transformName(app, it) }
    val date: LiveData<String> = liveFeedback.map { transformer.transformDate(app, it) }
    val errorCode: LiveData<String> = liveFeedback.map { transformer.transformErrorCode(app, it) }
    val errorDescription: LiveData<String> = liveFeedback.map { transformer.transformErrorDescription(app, it) }
    val liveShowError: LiveData<Boolean> = liveFeedback.map { it.error != null }
}
