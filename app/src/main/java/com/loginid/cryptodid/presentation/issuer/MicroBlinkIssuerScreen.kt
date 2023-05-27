package com.loginid.cryptodid.presentation.issuer

import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.loginid.cryptodid.presentation.home.microblink.ScanIDCard
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCEnteryState
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCViewModel
import com.loginid.cryptodid.presentation.theme.HomeBackGround
import com.microblink.blinkid.activity.result.ResultStatus
import com.microblink.blinkid.activity.result.TwoSideScanResult
import com.microblink.blinkid.activity.result.contract.TwoSideDocumentScan
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*



class MicroBlinkIssuerScreen constructor(
    val navController: NavController,
    val onScanResult: (Int) -> Unit,
){
    private var showProgress = true
    @Composable
    fun microBlinkScreen(
    ){

        val resultLauncher: ManagedActivityResultLauncher<Void?, TwoSideScanResult>  = rememberLauncherForActivityResult(TwoSideDocumentScan()) { twoSideScanResult: TwoSideScanResult ->
            when (twoSideScanResult.resultStatus) {
                ResultStatus.FINISHED -> {
                    // code after a successful scan
                    // use twoSideScanResult.result for fetching results, for example:
                    val ageVC = twoSideScanResult.result?.age
                    ageVC?.let {
                        //Here we can make use of the fetched vc
                        try {
                           onScanResult(it)
                        }catch (e: Exception){
                            Log.d("CompilerErr",e.message.toString())
                        }finally {
                            showProgress = false
                            navController.popBackStack()
                        }


                    }
                    Log.d("Extracted info", ageVC.toString())
                }
                ResultStatus.CANCELLED -> {
                    // code after a cancelled scan
                    showProgress = false
                    navController.popBackStack()
                }
                ResultStatus.EXCEPTION -> {
                    // code after a failed scan
                    Log.d("Extracted info", "errrrrrrrrrrorr")
                }
                else -> {

                }
            }
        }

        val scope = rememberCoroutineScope()
        LaunchedEffect(resultLauncher){
            scope.launch {
                delay(700)
                resultLauncher.launch(null)
            }
        }
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.HomeBackGround)) {

            if (showProgress) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }

}

