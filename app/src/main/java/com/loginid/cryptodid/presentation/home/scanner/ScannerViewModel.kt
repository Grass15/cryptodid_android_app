package com.loginid.cryptodid.presentation.home.scanner


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loginid.cryptodid.claimVerifier.VerificationStatus
import com.loginid.cryptodid.protocols.Issuer
import com.loginid.cryptodid.protocols.MG_FHE
import com.loginid.cryptodid.model.Claim
import com.loginid.cryptodid.claimVerifier.Verifier
import com.loginid.cryptodid.domain.repository.ScannerRepository
import com.loginid.cryptodid.domain.use_case.verify_vc.HouseRentaleVerificationUseCase
import com.loginid.cryptodid.utils.Resource
import com.loginid.cryptodid.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ScannerViewModel @Inject constructor(
      private val repository: ScannerRepository,
      private val houseRentaleVerificationUseCase: HouseRentaleVerificationUseCase
) : ViewModel() {

    private val _vState = MutableStateFlow(VerificationStatus())
    private val _state = MutableStateFlow(ScannerState())
    val state = _state.asStateFlow()
    val vState = _vState.asStateFlow()

    val verifier: Verifier =
        Verifier()

    /*init {
        startScanning()
    }*/

    fun startScanning(){
        viewModelScope.launch {
            repository.startScanning().collect{data ->
                if(!data.isNullOrBlank()){
                    _state.update { it.copy(
                        details = data
                    ) }

                    //adding url
                    verifier.setUrl(data)
                    startVerification(verifier)
                }
            }
        }
    }

    //Here we should configure our verify method so we can call it right after scanning
    fun setupVerifier(vc: Claim){

        //bank vc
        val fhe = MG_FHE(11, 512)
        val issuer1: Issuer =
            Issuer()
        issuer1.setAttribute(800)
            val BankVC: Claim = issuer1.getClaim("user_good","pass_good",fhe,"vcContent.issuerName","vcContent.VCType","vcContent.VCTitle","vcContent.VCContentOverview")// Claim(vcContent.VCTitle,vcContent.VCType,vcContent.issuerName,vcContent.VCContentOverview)
            BankVC.setFhe(fhe)

        //Creditscore

        val issuer2: Issuer =
            Issuer()
        issuer2.setAttribute((700) as Int)
        val CreditScoreVC: Claim = issuer2.getClaim("user_good","pass_good",fhe,"vcContent.issuerName","vcContent.VCType","vcContent.VCTitle","vcContent.VCContentOverview")// Claim(vcContent.VCTitle,vcContent.VCType,vcContent.issuerName,vcContent.VCContentOverview)
        CreditScoreVC.setFhe(fhe)

        val vcs: List<Claim> = listOf(vc,CreditScoreVC,BankVC)
      // verifier.AppendVCs(listOf(vc,CreditScoreVC,BankVC))
        verifier.setVc(vc)
        verifier.AppendVCs(vcs)

    }

    fun resetStatus(){
        _vState.update { it.copy(
            vMessage = "",
            vStatus = Status.NO_ACTION
        ) }
    }
    private fun startVerification(verifier: Verifier){
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

}