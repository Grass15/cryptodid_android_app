package com.loginid.cryptodid.presentation.home.biometrics

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

class BiometricsAuthenticationProvider(
    val context: Context,
    onBiometricFailled : (BiometricsSupportState) -> Unit,
    successFunc : () -> Unit)
{
    private val faceIDAuthenticator = FaceIDAuthenticator(context,onBiometricFailled,successFunc)
    private val fingerPrintAuthenticator = FingerPrintAuthenticator(context,onBiometricFailled,successFunc)

    @RequiresApi(Build.VERSION_CODES.P)
    fun getBiometricAuthenticator(selectedBiometric: BiomtricType): BiometricsAuthenticator?{
        return when(selectedBiometric){
            BiomtricType.FINGERPRINT -> {
                if(fingerPrintAuthenticator.checkBiometricSupport()) fingerPrintAuthenticator else null
            }
            BiomtricType.FACE_ID -> {
                if(faceIDAuthenticator.checkBiometricSupport()) faceIDAuthenticator else null
            }
            BiomtricType.AUTO -> {
                if (faceIDAuthenticator.isSupported()) {
                    faceIDAuthenticator
                } else if (fingerPrintAuthenticator.isSupported()) {
                    fingerPrintAuthenticator
                } else {
                    null
                }
            }
        }
    }
}