package com.learning.walletv21.presentation.home.microblink


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.walletv21.utils.Constants.BLINK_ID_LICENCE

import com.microblink.blinkid.MicroblinkSDK
import com.microblink.blinkid.activity.result.ResultStatus
import com.microblink.blinkid.activity.result.ScanResult
import com.microblink.blinkid.activity.result.TwoSideScanResult
import com.microblink.blinkid.activity.result.contract.TwoSideDocumentScan
import com.microblink.blinkid.entities.recognizers.RecognizerBundle
import com.microblink.blinkid.entities.recognizers.blinkid.generic.BlinkIdMultiSideRecognizer
import com.microblink.blinkid.fragment.overlay.blinkid.BlinkIdOverlaySettings


@Composable
fun ScanIDCard(
    onScanResult: (Int) -> Unit,
    launchCamera: (ManagedActivityResultLauncher<Void?,TwoSideScanResult>) -> Unit
) {

    // Set the Microblink SDK license file
   // MicroblinkSDK.setLicenseKey(BLINK_ID_LICENCE, context)

    // Create a result launcher using the TwoSideDocumentScan activity result contract
    val resultLauncher = rememberLauncherForActivityResult(TwoSideDocumentScan()) { twoSideScanResult: TwoSideScanResult ->
        when (twoSideScanResult.resultStatus) {
            ResultStatus.FINISHED -> {
                // code after a successful scan
                // use twoSideScanResult.result for fetching results, for example:
                val ageVC = twoSideScanResult.result?.age
                ageVC?.let { onScanResult(it) }
                //Log.d("Extracted info", ageVC.toString())
            }
            ResultStatus.CANCELLED -> {
                // code after a cancelled scan
            }
            ResultStatus.EXCEPTION -> {
                // code after a failed scan
                Log.d("Extracted info", "errrrrrrrrrrrrrrrrrrrrrrrrorr")
            }
            else -> {}
        }
    }

    // Call the result launcher to start the scan process
    val buttonClickHandler = {
        //launchCamera(resultLauncher)
        launchCamera(resultLauncher)
        //resultLauncher.launch(null)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                buttonClickHandler.invoke()
            }
            .padding(16.dp)
    ){
        Icon(imageVector = Icons.Filled.CardMembership, contentDescription = "get age credentials using microblink")
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = "Get Age VC",modifier = Modifier.weight(1f), style = TextStyle(fontSize = 18.sp))
    }


}

