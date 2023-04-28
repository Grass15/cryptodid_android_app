package com.learning.walletv21.presentation.home.microblink


import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    context: Context,
    onScanResult: (ScanResult) -> Unit
) {
    // Set the Microblink SDK license file
   // MicroblinkSDK.setLicenseKey(BLINK_ID_LICENCE, context)

    // Create a result launcher using the TwoSideDocumentScan activity result contract
    val resultLauncher = rememberLauncherForActivityResult(TwoSideDocumentScan()) { twoSideScanResult: TwoSideScanResult ->
        when (twoSideScanResult.resultStatus) {
            ResultStatus.FINISHED -> {
                // code after a successful scan
                // use twoSideScanResult.result for fetching results, for example:
                val firstName = twoSideScanResult.result?.firstName?.value()
                Log.d("Extracted info", firstName.toString())
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
        resultLauncher.launch(null)
    }

    Button(
        onClick = { buttonClickHandler.invoke() },
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Scan ID Card")
    }

}
