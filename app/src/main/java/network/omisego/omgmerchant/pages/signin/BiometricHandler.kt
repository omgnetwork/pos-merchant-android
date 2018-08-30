package network.omisego.omgmerchant.pages.signin

import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.os.CancellationSignal
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.support.annotation.RequiresApi
import network.omisego.omgmerchant.extensions.logi
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class BiometricHandler {
    private var keyStore: KeyStore? = null
    private var cipher: Cipher? = null

    companion object {
        val KEY_NAME = "OMG_MERCHANT_BIOMETRIC_KEY"
    }

    fun createCancellationSignal(): CancellationSignal {
        return CancellationSignal().apply {
            setOnCancelListener { logi("Cancel") }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun createCryptoObject(): BiometricPrompt.CryptoObject {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            createSecretKey()
            if (createCipher()) {
                return BiometricPrompt.CryptoObject(cipher!!)
            } else {
                throw RuntimeException("Your device is currently not support biometric feature.")
            }
        } else {
            throw RuntimeException("Your device is currently not support biometric feature.")
        }
    }

    @RequiresApi(M)
    private fun createSecretKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore?.load(null)

            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            keyGenerator.init(KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setRandomizedEncryptionRequired(false)
                .build())

            keyGenerator.generateKey()
        } catch (exc: KeyStoreException) {
            exc.printStackTrace()
        } catch (exc: NoSuchAlgorithmException) {
            exc.printStackTrace()
        } catch (exc: NoSuchProviderException) {
            exc.printStackTrace()
        } catch (exc: InvalidAlgorithmParameterException) {
            exc.printStackTrace()
        } catch (exc: CertificateException) {
            exc.printStackTrace()
        } catch (exc: IOException) {
            exc.printStackTrace()
        }
    }

    @RequiresApi(M)
    private fun createCipher(): Boolean {
        try {
            cipher = Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES + "/" +
                    KeyProperties.BLOCK_MODE_CBC + "/" +
                    KeyProperties.ENCRYPTION_PADDING_PKCS7
            )
        } catch (e: Exception) {
            throw RuntimeException("Failed to init Cipher", e)
        }

        return try {
            keyStore?.load(null)
            val key = keyStore?.getKey(KEY_NAME, null)
            cipher?.init(Cipher.ENCRYPT_MODE, key)
            true
        } catch (e: KeyPermanentlyInvalidatedException) {
            false
        } catch (e: Exception) {
            throw RuntimeException("Failed to init Cipher", e)
        }
    }
}