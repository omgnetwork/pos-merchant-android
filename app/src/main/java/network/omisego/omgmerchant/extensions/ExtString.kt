package network.omisego.omgmerchant.extensions

import co.omisego.omisego.security.OMGKeyManager

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

infix fun String.encryptWith(keyManager: OMGKeyManager) = keyManager.encrypt(this)
infix fun String.decryptWith(keyManager: OMGKeyManager) = keyManager.decrypt(this)