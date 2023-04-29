package com.learning.walletv21.presentation.home.biometrics


import android.Manifest
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class BiometricAuthenticator(val context: Context,val successFunc : () -> Unit) {

    private var cancellationSignal: CancellationSignal? = null

    @RequiresApi(Build.VERSION_CODES.P)
    fun authenticate(activity: Activity) {
        if (checkBiometricSupport()) {
            val biometricPrompt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                BiometricPrompt
                    .Builder(context)
                    .setTitle("Allow Biometric Authentication")
                    .setSubtitle("You will no longer need username and password during login")
                    .setDescription("We use biometric authentication to protect your data")
                    .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                    .setNegativeButton("Not Now", activity.mainExecutor) { _, _ ->
                        notifyUser("Authentication cancelled")
                    }
                    .build()
            } else {
                BiometricPrompt
                    .Builder(context)
                    .setTitle("Allow Biometric Authentication")
                    .setSubtitle("You will no longer need username and password during login")
                    .setDescription("We use biometric authentication to protect your data")
                    .setNegativeButton("Not Now", activity.mainExecutor) { _, _ ->
                        notifyUser("Authentication cancelled")
                    }
                    .build()
            }

            biometricPrompt.authenticate(getCancellationSignal(), activity.mainExecutor, getAuthenticationCallback())
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isDeviceSecure) {
            notifyUser("Lock screen security not enabled in the settings")
            return false
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as AppCompatActivity, arrayOf(Manifest.permission.USE_BIOMETRIC), 1)
            notifyUser("Fingerprint authentication permission not enabled")
            return false
        }

        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            notifyUser("Device does not support fingerprint authentication")
            return false
        }

        return true
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication cancelled via signal")
        }
        return cancellationSignal as CancellationSignal
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun getAuthenticationCallback(): BiometricPrompt.AuthenticationCallback {
        return object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                notifyUser("Authentication error $errorCode: $errString")
            }

            override fun onAuthenticationFailed() {
                notifyUser("Authentication failed")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
              //  successFunc.run {
                    successFunc()
                    notifyUser("Authentication succeeded")
               // }
            }
        }
    }

    private fun notifyUser(message: String) {
        Log.d("BiometricAuthenticator", message)
    }
}