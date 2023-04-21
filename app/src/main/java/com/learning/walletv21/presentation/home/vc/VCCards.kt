package com.learning.walletv21.presentation.home.vc

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.learning.walletv21.presentation.home.scanner.ScannerViewModel
import com.learning.walletv21.presentation.home.vc.VCViewModel.VCViewModel


//@ExperimentalMaterialApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VCCard(
    vcViewModel: VCViewModel = hiltViewModel()
) {
    //Scanner
    val scannerViewModel: ScannerViewModel = hiltViewModel()
    val scannedText = scannerViewModel.state.collectAsState()

    //vcState
    val vcstate = vcViewModel.vcDataState.collectAsState()
    val vcActionState = vcViewModel.vcAction.collectAsState()

          LazyColumn(modifier = Modifier.padding(12.dp)){
              items(vcstate.value){vc->
                  vc?.let {
                      CardSwiper(
                          VCState = it,
                          onDeleteButtonClicked = {
                              Log.d("IID","$it")
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
                          onVerifyButtonClicked = {scannerViewModel.startScanning()}
                      )
                  }
                  Spacer(modifier = Modifier.height(10.dp))
              }
          }

}



/*

@Preview(showBackground = true)
@Composable
fun previewVCCard(){
    VCCard()
}*/