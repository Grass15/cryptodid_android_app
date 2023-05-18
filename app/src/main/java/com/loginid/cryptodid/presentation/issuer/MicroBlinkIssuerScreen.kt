package com.loginid.cryptodid.presentation.issuer

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.loginid.cryptodid.presentation.home.microblink.ScanIDCard
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCEnteryState
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCViewModel
import java.util.*

@Composable
fun microBlinkScreen(
    vcViewModel: VCViewModel = hiltViewModel()
){
    ScanIDCard(
        onScanResult = {
            Log.d("Extracted",it.toString())
            vcViewModel.saveVC(
                VCEnteryState(
                    experationDate = Date(),
                    issuerName = "MicroBlink",
                    VCType = "Personal data",
                    VCTitle = "Age",
                    VCContentOverview = "+18",
                    VCAttribute = it
                )
            )
        },
        launchCamera = {
            it.launch(null)
        })
}