package com.loginid.cryptodid.presentation.home.scanner.ScannerStrategy

import android.util.Log
import com.loginid.cryptodid.claimVerifier.VerificationStatus
import com.loginid.cryptodid.claimVerifier.Verifier
import com.loginid.cryptodid.model.Claim
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class emptyScanner: Scanner {
    override fun startScanning() {
        Log.d("Scanner","StartScanning")
    }

    override fun setupVerifier(vc: Claim) {
        Log.d("Scanner","Setup Verifier ${vc.id}")
    }

    override fun startVerification(verifier: Verifier) {
        Log.d("Scanner","start Verification ${verifier.toString()}")
    }

    override fun resetStatus() {
        Log.d("Scanner","Reseting status")
    }

    override fun displayScannerType() {
        Log.d("ChangingScanner","To empty Viewmodel")
    }

    override fun getVerificationStatus(): StateFlow<VerificationStatus> {
        return MutableStateFlow(VerificationStatus()).asStateFlow()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        val otherScanner = other as Scanner

        // Implement your own logic to compare properties for equality
        // Return false if any properties are not equal

        // For example:
        // if (property1 != otherScanner.property1) return false
        // if (property2 != otherScanner.property2) return false

        return true
    }

}