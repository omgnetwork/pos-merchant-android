package network.omisego.omgmerchant.model

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 17/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.Parcelable
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Transaction
import co.omisego.omisego.model.TransactionSource
import co.omisego.omisego.model.Wallet
import kotlinx.android.parcel.Parcelize
import network.omisego.omgmerchant.pages.authorized.confirm.ConfirmFragmentArgs
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import java.util.Date

@Parcelize
data class Feedback(
    val success: Boolean,
    val transactionType: String,
    val createdAt: Date,
    val source: TransactionSource,
    val error: APIError? = null
) : Parcelable {
    companion object {
        fun success(transactionType: String, transaction: Transaction): Feedback {
            return if (transactionType.equals(SCAN_RECEIVE, true)) Feedback(
                true,
                transactionType,
                transaction.createdAt,
                transaction.from
            ) else {
                Feedback(
                    true,
                    transactionType,
                    transaction.createdAt,
                    transaction.to
                )
            }
        }

        fun error(args: ConfirmFragmentArgs, wallet: Wallet, error: APIError?): Feedback {
            val source = TransactionSource(
                wallet.address,
                args.amount.toBigDecimal().multiply(args.token.subunitToUnit),
                args.token.id,
                args.token,
                wallet.userId,
                wallet.user,
                null,
                null
            )

            return if (args.transactionType.equals(SCAN_RECEIVE, true)) Feedback(
                false,
                args.transactionType,
                Date(),
                source,
                error
            ) else {
                Feedback(
                    false,
                    args.transactionType,
                    Date(),
                    source,
                    error
                )
            }
        }
    }
}
