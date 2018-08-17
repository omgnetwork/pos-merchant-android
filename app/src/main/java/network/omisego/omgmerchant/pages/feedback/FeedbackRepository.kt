package network.omisego.omgmerchant.pages.feedback

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 17/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import network.omisego.omgmerchant.storage.Storage

class FeedbackRepository {
    fun deleteFeedback() {
        Storage.deleteFeedback()
    }
}
