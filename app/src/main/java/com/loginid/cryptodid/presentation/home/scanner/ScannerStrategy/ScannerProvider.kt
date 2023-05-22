package com.loginid.cryptodid.presentation.home.scanner.ScannerStrategy


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.loginid.cryptodid.presentation.home.scanner.ScannerViewModel
import com.loginid.cryptodid.presentation.issuer.InsuranceNumber.SINViewModel
import com.loginid.cryptodid.presentation.issuer.voting.VotingViewModel

class ScannerProvider(
    var vcScanner: ScannerViewModel,
    var votingScanner: VotingViewModel,
    var sinScanner: SINViewModel
) {

     fun getScanner(type: String):Scanner{
        if(type == "vc"){
            return vcScanner
        }else if(type == "sin"){
            return sinScanner
        }else{
            return votingScanner
        }
    }
}