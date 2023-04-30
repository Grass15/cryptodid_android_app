package com.learning.walletv21.presentation.home.scanner


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.walletv21.core.protocols.Issuer
import com.learning.walletv21.core.protocols.MG_FHE
import com.learning.walletv21.core.protocols.javamodels.Claim
import com.learning.walletv21.core.protocols.verifier.Verifier
import com.learning.walletv21.domain.repository.ScannerRepository
import com.learning.walletv21.presentation.home.vc.VCViewModel.VCEnteryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.ParseException
import javax.inject.Inject


@HiltViewModel
class ScannerViewModel @Inject constructor(
      private val repository: ScannerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ScannerState())
    val state = _state.asStateFlow()
    val verifier: Verifier = Verifier()

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
                    startVerification()
                }
            }
        }
    }

    //Here we should configure our verify method so we can call it right after scanning
    fun setupVerifier(vc: Claim){

        //bank vc
    val fhe = MG_FHE(11, 512)
    val issuer1: Issuer = Issuer()
    issuer1.setAttribute(200)
        val BankVC: Claim = issuer1.getClaim("user_good","pass_good",fhe,"vcContent.issuerName","vcContent.VCType","vcContent.VCTitle","vcContent.VCContentOverview")// Claim(vcContent.VCTitle,vcContent.VCType,vcContent.issuerName,vcContent.VCContentOverview)
        BankVC.setFhe(fhe)

        //Creditscore

        val issuer2: Issuer = Issuer()
        issuer2.setAttribute(900)
        val CreditScoreVC: Claim = issuer2.getClaim("user_good","pass_good",fhe,"vcContent.issuerName","vcContent.VCType","vcContent.VCTitle","vcContent.VCContentOverview")// Claim(vcContent.VCTitle,vcContent.VCType,vcContent.issuerName,vcContent.VCContentOverview)
        CreditScoreVC.setFhe(fhe)

        val vcs: List<Claim> = listOf(vc,CreditScoreVC,BankVC)
      // verifier.AppendVCs(listOf(vc,CreditScoreVC,BankVC))
        verifier.setVc(vc)
        verifier.AppendVCs(vcs)

        Log.d("Verif",verifier.vcs.get(1).title.toString())

    }

    fun startVerification(){
        //verifiying
        Log.d("Verifier",verifier.toString())
        Log.d("Verifier",verifier.vcs.get(0).toString())
        Log.d("Verifier",verifier.vcs.get(1).toString())
        Log.d("Verifier",verifier.vcs.get(2).toString())
        try {
           verifier.verifyMultipleVCs()
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

}