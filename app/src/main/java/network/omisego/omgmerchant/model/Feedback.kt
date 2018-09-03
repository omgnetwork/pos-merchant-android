package network.omisego.omgmerchant.model

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 17/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.Parcelable
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.transaction.TransactionSource
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class Feedback(
    val success: Boolean,
    val transactionType: String,
    val createdAt: Date,
    val source: TransactionSource,
    val error: APIError? = null
    ) : Parcelable
