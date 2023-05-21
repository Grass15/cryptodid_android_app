package com.loginid.cryptodid.presentation.home.scanner.ScannerStrategy

import com.loginid.cryptodid.claimVerifier.VerificationStatus
import com.loginid.cryptodid.claimVerifier.Verifier
import com.loginid.cryptodid.model.Claim
import com.loginid.cryptodid.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

interface Scanner {
    fun startScanning()
    fun setupVerifier(vc: Claim)
    fun startVerification(verifier: Verifier)
    fun resetStatus()
    fun displayScannerType()
    fun getVerificationStatus(): StateFlow<VerificationStatus>
}