package com.loginid.cryptodid.presentation.home.biometrics


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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat





class FingerPrintAuthenticator(val context: Context, val onBiometricFailled : (BiometricsSupportState) -> Unit, val successFunc : () -> Unit)
    :BiometricsAuthenticator
{

    private var cancellationSignal: CancellationSignal? = null
    private val permettedErrors: List<Int> = listOf(10)

    @RequiresApi(Build.VERSION_CODES.P)
    override fun authenticate(activity: Activity) {
        if (checkBiometricSupport()) {
            val biometricPrompt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                BiometricPrompt
                    .Builder(context)
                    .setTitle("Allow Biometric Authentication")
                    .setSubtitle("In order to verify/pull a vc you should verify your identity")
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
                    .setSubtitle("In order to verify/pull a vc you should verify your identity")
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
     override fun checkBiometricSupport(): Boolean {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isDeviceSecure) {
            notifyUser("Lock screen security not enabled in the settings")
            onBiometricFailled(BiometricsSupportState(
                false,
                "Lock screen security not enabled in the settings"
            ))
            return false
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.USE_BIOMETRIC), 1)
            notifyUser("Fingerprint authentication permission not enabled")
            onBiometricFailled(BiometricsSupportState(
                false,
                "Fingerprint authentication permission not enabled"
            ))
            return false
        }

        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            notifyUser("Device does not support fingerprint authentication")
            onBiometricFailled(BiometricsSupportState(
                false,
                "Device does not support fingerprint authentication"
            ))
            return false
        }


        return true
    }

    override fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication cancelled via signal")
        }
        return cancellationSignal as CancellationSignal
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun getAuthenticationCallback(): BiometricPrompt.AuthenticationCallback {
        return object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                notifyUser("Authentication error $errorCode: $errString")
                if(errorCode !in permettedErrors){
                    onBiometricFailled(BiometricsSupportState(
                        false,
                        "Authentication error $errorCode: $errString"
                    ))
                }
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

    @RequiresApi(Build.VERSION_CODES.P)
    override fun isSupported(): Boolean {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isDeviceSecure) {
            return false
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.USE_BIOMETRIC), 1)
            return false
        }

        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            return false
        }


        return true
    }

    private fun notifyUser(message: String) {
        Log.d("BiometricAuthenticator", message)
    }
}
