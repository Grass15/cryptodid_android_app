package com.loginid.cryptodid.presentation.issuer.InsuranceNumber

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loginid.cryptodid.claimVerifier.VerificationStatus
import com.loginid.cryptodid.claimVerifier.Verifier
import com.loginid.cryptodid.domain.repository.ScannerRepository
import com.loginid.cryptodid.domain.use_case.verify_vc.AccessControlUseCase
import com.loginid.cryptodid.domain.use_case.verify_vc.HouseRentaleVerificationUseCase
import com.loginid.cryptodid.domain.use_case.verify_vc.privilege.VotingUseCase
import com.loginid.cryptodid.model.Claim
import com.loginid.cryptodid.presentation.home.scanner.ScannerState
import com.loginid.cryptodid.presentation.home.scanner.ScannerStrategy.Scanner
import com.loginid.cryptodid.utils.Resource
import com.loginid.cryptodid.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


//Here we will invoke the use case corresponding to the verification of vote
@HiltViewModel
class SINViewModel @Inject constructor(
    private val repository: ScannerRepository,
    private val accessControlUseCase: AccessControlUseCase
) : ViewModel(),Scanner {

    private val _vState = MutableStateFlow(VerificationStatus())
    private val _state = MutableStateFlow(ScannerState())
    val state = _state.asStateFlow()
    val vState = _vState.asStateFlow()

    val verifier: Verifier =
        Verifier()

    /*init {
        startScanning()
    }*/

    override fun startScanning(){
        resetStatus()
        viewModelScope.launch {
            verifier.setUrl("192.168.1.10:8080")
            startVerification(verifier)
//            repository.startScanning().collect{data ->
//                if(!data.isNullOrBlank()){
//                    _state.update { it.copy(
//                        details = data
//                    ) }
//
//                    //adding url
//                    verifier.setUrl(data)
//                    startVerification(verifier)
//                }
//            }
        }
    }

    //Here we should configure our verify method so we can call it right after scanning
    override fun setupVerifier(vc: Claim){

        //bank vc
//        val fhe = MG_FHE(11, 512)
//        val issuer1: Issuer =
//            Issuer()
//        issuer1.setAttribute(800)
//            val BankVC: Claim = issuer1.getClaim("user_good","pass_good",fhe,"vcContent.issuerName","vcContent.VCType","vcContent.VCTitle","vcContent.VCContentOverview")// Claim(vcContent.VCTitle,vcContent.VCType,vcContent.issuerName,vcContent.VCContentOverview)
//            //BankVC.setFhe(fhe)
//
//        //Creditscore
//
//        val issuer2: Issuer =
//            Issuer()
//        issuer2.setAttribute((700) as Int)
//        val CreditScoreVC: Claim = issuer2.getClaim("user_good","pass_good",fhe,"vcContent.issuerName","vcContent.VCType","vcContent.VCTitle","vcContent.VCContentOverview")// Claim(vcContent.VCTitle,vcContent.VCType,vcContent.issuerName,vcContent.VCContentOverview)
//        CreditScoreVC.setFhe(fhe)
//
//        val vcs: List<Claim> = listOf(vc,CreditScoreVC,BankVC)
//      // verifier.AppendVCs(listOf(vc,CreditScoreVC,BankVC))
//        verifier.setVc(vc)
//        verifier.AppendVCs(vcs)

    }

    override fun resetStatus(){
        _vState.update { it.copy(
            vMessage = "",
            vStatus = Status.NO_ACTION
        ) }
    }

    override fun displayScannerType() {
        Log.d("ChangingScanner","To vcScanner Viewmodel")
    }

    override fun getVerificationStatus(): StateFlow<VerificationStatus> {
        return vState
    }

    override fun startVerification(verifier: Verifier){
        accessControlUseCase(verifier).onEach { result ->
            when(result){
                is Resource.Error -> {
                    _vState.update { it.copy(
                        vMessage = result.message.toString(),
                        vStatus = Status.ERROR
                    ) }
                }
                is Resource.Loading -> {
                    _vState.update { it.copy(
                        vMessage = "Waiting verification",
                        vStatus = Status.LOADING
                    ) }
                }
                is Resource.Success -> {
                    _vState.update { it.copy(
                        vMessage = result.data!!.vMessage,
                        vStatus = result.data!!.vStatus
                    ) }
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.IO))
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