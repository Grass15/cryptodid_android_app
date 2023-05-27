package com.loginid.cryptodid.presentation.home.biometrics

import android.app.Activity
import android.hardware.biometrics.BiometricPrompt
import android.os.CancellationSignal

interface BiometricsAuthenticator {

    fun checkBiometricSupport(): Boolean

    fun authenticate(activity: Activity)

    fun getCancellationSignal() : CancellationSignal

    fun getAuthenticationCallback() : BiometricPrompt.AuthenticationCallback

    fun isSupported(): Boolean

}