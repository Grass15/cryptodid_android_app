package com.learning.walletv21.presentation.home.scanner

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collect

@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel = hiltViewModel(),
    onQrScanned: (String) -> Unit
){
    val state = viewModel.state.collectAsState()
    onQrScanned(state.value.details)

    /*

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .weight(0.5f), contentAlignment = Alignment.Center) {
            Text(text =  state.value.details )
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .weight(0.5f), contentAlignment = Alignment.BottomCenter) {
            Button(onClick = { viewModel.startScanning() }) {
                Text(text = "start scanning")
            }
        }


    }*/

}