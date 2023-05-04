package com.loginid.cryptodid.presentation.home.biometrics


import android.Manifest
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat




class FaceIDAuthenticator constructor(val context: Context,val onBiometricFailled : (BiometricsSupportState) -> Unit, val successFunc : () -> Unit)
    :BiometricsAuthenticator
{

    @RequiresApi(Build.VERSION_CODES.P)
    override fun checkBiometricSupport(): Boolean {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isDeviceSecure) {
            Log.d("FaceAuthenticator", "Lock screen security not enabled in the settings")
            onBiometricFailled(BiometricsSupportState(
                false,
                "Lock screen security not enabled in the settings"
            ))
            return false
        }

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.USE_BIOMETRIC),
                1
            )
            Log.d("FaceAuthenticator", "Fingerprint authentication permission not enabled")
            onBiometricFailled(BiometricsSupportState(
                false,
                "Fingerprint authentication permission not enabled"
            ))
            return false
        }

        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_FACE)) {
            Log.d("FaceAuthenticator", "Device does not support face authentication")
            onBiometricFailled(BiometricsSupportState(
                false,
                "Device does not support face authentication"
            ))
            return false
        }

        return true
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun authenticate(activity: Activity) {
        if (!checkBiometricSupport()) {
            return
        }

        val biometricPrompt = BiometricPrompt
            .Builder(context)
            .setTitle("Allow Face Authentication")
            .setSubtitle("You will no longer need username and password during login")
            .setDescription("We use face authentication to protect your data")
            .setNegativeButton("Not Now", activity.mainExecutor) { _, _ ->
                Log.d("FaceAuthenticator", "Authentication cancelled")
            }
            .build()

        biometricPrompt.authenticate(getCancellationSignal(), activity.mainExecutor, getAuthenticationCallback())


    }

    override fun getCancellationSignal(): CancellationSignal {
        val cancellationSignal = CancellationSignal()
        cancellationSignal.setOnCancelListener {
            Log.d("FaceAuthenticator", "Authentication cancelled via signal")
        }
        return cancellationSignal
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun getAuthenticationCallback(): BiometricPrompt.AuthenticationCallback {
        return object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                Log.d("FaceAuthenticator", "Authentication error $errorCode: $errString")
            }

            override fun onAuthenticationFailed() {
                Log.d("FaceAuthenticator", "Authentication failed")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                Log.d("FaceAuthenticator", "Authentication succeeded")
                successFunc()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun isSupported(): Boolean {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isDeviceSecure) {
            return false
        }

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.USE_BIOMETRIC),
                1
            )
            return false
        }

        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_FACE)) {
            return false
        }

        return true
    }


}