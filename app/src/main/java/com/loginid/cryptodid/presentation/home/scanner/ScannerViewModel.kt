package com.loginid.cryptodid.presentation.home.scanner


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loginid.cryptodid.claimVerifier.VerificationStatus
import com.loginid.cryptodid.model.Claim
import com.loginid.cryptodid.claimVerifier.Verifier
import com.loginid.cryptodid.data.local.entity.VCType
import com.loginid.cryptodid.data.repository.UserDataStoreRepository
import com.loginid.cryptodid.domain.repository.ScannerRepository
import com.loginid.cryptodid.domain.repository.UserRepository
import com.loginid.cryptodid.domain.use_case.verify_vc.HouseRentaleVerificationUseCase
import com.loginid.cryptodid.presentation.home.scanner.ScannerStrategy.Scanner
import com.loginid.cryptodid.utils.Resource
import com.loginid.cryptodid.utils.Status
import com.loginid.cryptodid.utils.UserDataPrefrence
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ScannerViewModel @Inject constructor(
      private val repository: ScannerRepository,
      private val houseRentaleVerificationUseCase: HouseRentaleVerificationUseCase,
      private val userRepooitory: UserRepository,
      private val dataStoreRepository: UserDataStoreRepository,
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
            verifier.setUrl("192.168.1.15:8080")
            startVerification(verifier)
//            repository.startScanning().collect{data ->
//                if(!data.isNullOrBlank()){
//                    _state.update { it.copy(
//                        details = data
//                    ) }
//                    //adding url
//                    verifier.setUrl(data)
//                    startVerification(verifier)
//                }
//            }
        }
    }

    //Here we should configure our verify method so we can call it right after scanning
    override fun setupVerifier(vc: Claim){

        viewModelScope.launch {
            dataStoreRepository.readUserDataState().collect{userData ->
                userData.userName.let {
                    val data = userRepooitory.getUserByUserName(it)
                    data.let {
                        verifier.AddUserPresentation(listOf(it.username,it.firstname,it.lastname))
                        verifier.AppendVC("creditScore", userRepooitory.getVCByType(VCType.CREDIT_SCORE))
                        verifier.AppendVC("age", userRepooitory.getVCByType(VCType.DATE_OF_BIRTH))
                        verifier.AppendVC("balance", userRepooitory.getVCByType(VCType.BANK))
                    }
                }
            }


        }
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
        houseRentaleVerificationUseCase(verifier).onEach { result ->
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


    /*
    fun startVerification(){
        //verifiying
       /* Log.d("Verifier",verifier.toString())
        Log.d("Verifier",verifier.vcs.get(0).toString())
        Log.d("Verifier",verifier.vcs.get(1).toString())
        Log.d("Verifier",verifier.vcs.get(2).toString())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                verifier.verifyMultipleVCs()
                Log.d("Verifyer","From scanner Starting verification")
            } catch (e: Exception){
                Log.d("Verifyer","From scanner exception")
                e.printStackTrace()
            }
        }
        */
        Thread{
            try {
                verifier.verifyMultipleVCs()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }.start()

    }
*/

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