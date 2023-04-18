package com.learning.walletv21.presentation.home.vc

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.learning.walletv21.presentation.home.vc.VCViewModel.VCState
import com.learning.walletv21.presentation.home.vc.VCViewModel.VCViewModel


//@ExperimentalMaterialApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VCCard(
    vcViewModel: VCViewModel = hiltViewModel()
) {
    val vcstate = vcViewModel.vcDataState.collectAsState()

          LazyColumn(modifier = Modifier.padding(12.dp)){
              items(vcstate.value){vc->
                  vc?.let {
                      CardSwiper(
                          VCState = it,
                          onDeleteButtonClicked = {},
                          onVerifyButtonClicked = {}
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