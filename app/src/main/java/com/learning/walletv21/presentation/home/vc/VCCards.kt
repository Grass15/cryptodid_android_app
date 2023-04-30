package com.learning.walletv21.presentation.home.vc

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.learning.walletv21.presentation.home.biometrics.BiometricAuthenticator
import com.learning.walletv21.presentation.home.scanner.ScannerViewModel
import com.learning.walletv21.presentation.home.vc.VCViewModel.VCViewModel


//@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VCCard(
    vcViewModel: VCViewModel = hiltViewModel()
) {
    //Scanner
    val scannerViewModel: ScannerViewModel = hiltViewModel()
    val scannedText = scannerViewModel.state.collectAsState()

    //Biometrics Prompt
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val biometricAuthenticator = remember { BiometricAuthenticator(context,
        { scannerViewModel.startScanning() }) }
    var showPrompt by remember { mutableStateOf(false) }

    //vcState
    val vcstate = vcViewModel.vcDataState.collectAsState()
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
                                  showPrompt = true
                              }
                          }
                      )
                  }
                  Spacer(modifier = Modifier.height(10.dp))
                  /**
                   * Just to test verification outside of the couroutine scope
                   */
                  /*
                  Text(text = scannedText.value.details, modifier = Modifier.clickable {
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

}



/*

@Preview(showBackground = true)
@Composable
fun previewVCCard(){
    VCCard()
}*/