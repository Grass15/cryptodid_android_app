package com.loginid.cryptodid.presentation.home.vc

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loginid.cryptodid.claimVerifier.VerificationStatus
import com.loginid.cryptodid.presentation.home.biometrics.BiometricAuthenticator
import com.loginid.cryptodid.presentation.home.scanner.ScannerViewModel
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCViewModel
import com.loginid.cryptodid.utils.Status


//@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VCCard(
    vcViewModel: VCViewModel = hiltViewModel(),
    onVerificationStateAction: (VerificationStatus) -> Unit
) {
    //Scanner
    val scannerViewModel: ScannerViewModel = hiltViewModel()
    val scannedText = scannerViewModel.state.collectAsState()

    //Biometrics Prompt
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val biometricAuthenticator = remember { BiometricAuthenticator(context,
        onBiometricFailled = {}
        )
    { scannerViewModel.startScanning() } }
    var showPrompt by remember { mutableStateOf(false) }

    //States
    val vcstate = vcViewModel.vcDataState.collectAsState()
    val verificationState = scannerViewModel.vState.collectAsState()
    val vcActionState = vcViewModel.vcAction.collectAsState()


          LazyColumn(modifier = Modifier.padding(12.dp)){
              items(vcstate.value){vc->
                  vc?.let {
                      CardSwiper(
                          VCState = it,
                          onDeleteButtonClicked = {
                                          vcViewModel.deleteVC(it.VCID).run {
                                              /*when(vcActionState.value.status){
                                                  Status.ERROR -> TODO()
                                                  Status.SUCCESS -> TODO()
                                                  Status.FAILLED -> TODO()
                                                  Status.LOADING -> TODO()
                                                  Status.NO_ACTION -> TODO()
                                              }*/
                                          }
                          },
                          onVerifyButtonClicked = {
                              it.rawVC?.let {
                                      it1 -> scannerViewModel.setupVerifier(it1)
                                  scannerViewModel.resetStatus()
                                  showPrompt = true
                              }
                          }
                      )
                  }
                  Spacer(modifier = Modifier.height(10.dp))
                  /*
                   * Just to test verification outside of the couroutine scope
                   */
                  /*
                  Text(text = "https://www.google.com", modifier = Modifier.clickable {
                      scannerViewModel.startVerification()
                  })
                 */
              }
          }

    //scannerViewModel.startScanning()

    if (showPrompt) {
        LaunchedEffect(true) {
            biometricAuthenticator.authenticate(activity)
            showPrompt = false
        }
    }

    when(verificationState.value.vStatus){
        Status.ERROR -> {
            Log.d("Verification",verificationState.value.vMessage)
            onVerificationStateAction(verificationState.value)
        }
        Status.SUCCESS -> {
            Log.d("Verification",verificationState.value.vMessage)
            onVerificationStateAction(verificationState.value)
        }
        Status.FAILLED -> {
            Log.d("Verification",verificationState.value.vMessage)
        }
        Status.LOADING -> {
            Log.d("Verification",verificationState.value.vMessage)
            onVerificationStateAction(verificationState.value)
        }
        Status.NO_ACTION -> {
            Log.d("Verification",verificationState.value.vMessage)
            onVerificationStateAction(verificationState.value)
        }

    }

}



/*

@Preview(showBackground = true)
@Composable
fun previewVCCard(){
    VCCard()
}*/