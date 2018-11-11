package network.omisego.omgmerchant.model

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 17/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.Parcelable
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.Transaction
import co.omisego.omisego.model.TransactionConsumption
import co.omisego.omisego.model.TransactionSource
import co.omisego.omisego.model.User
import kotlinx.android.parcel.Parcelize
import network.omisego.omgmerchant.pages.authorized.confirm.ConfirmFragmentArgs
import network.omisego.omgmerchant.pages.authorized.scan.SCAN_RECEIVE
import network.omisego.omgmerchant.pages.authorized.scan.ScanFragmentArgs
import java.math.BigDecimal
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

        fun success(transactionType: String, transactionConsumption: TransactionConsumption): Feedback {
            return Feedback(
                true,
                transactionType,
                transactionConsumption.createdAt!!,
                TransactionSource(
                    transactionConsumption.transactionRequest.user?.username
                        ?: transactionConsumption.transactionRequest.address
                        ?: transactionConsumption.transactionRequest.user?.email!!,
                    amount = transactionConsumption.estimatedConsumptionAmount,
                    tokenId = transactionConsumption.token.id,
                    token = transactionConsumption.token,
                    userId = transactionConsumption.transactionRequest.user?.id,
                    user = transactionConsumption.transactionRequest.user,
                    accountId = null,
                    account = null
                )
            )
        }

        fun error(args: ScanFragmentArgs, address: String?, user: User?, error: APIError?): Feedback {
            return createError(args.token, args.transactionType, args.amount.toBigDecimal(), address, user, error)
        }

        fun error(args: ConfirmFragmentArgs, address: String?, user: User?, error: APIError?): Feedback {
            return createError(args.token, args.transactionType, args.amount.toBigDecimal(), address, user, error)
        }

        private fun createError(
            token: Token,
            transactionType: String,
            amount: BigDecimal,
            address: String?,
            user: User?,
            error: APIError?
        ): Feedback {
            val source = TransactionSource(
                address ?: "-",
                AmountFormat.Unit(amount, token.subunitToUnit).toSubunit().amount,
                token.id,
                token,
                user?.id,
                user,
                null,
                null
            )

            return if (transactionType.equals(SCAN_RECEIVE, true)) Feedback(
                false,
                transactionType,
                Date(),
                source,
                error
            ) else {
                Feedback(
                    false,
                    transactionType,
                    Date(),
                    source,
                    error
                )
            }
        }
    }
}
