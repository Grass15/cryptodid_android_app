package com.learning.walletv21.presentation.home.microblink

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun MyScreen() {
    val context = LocalContext.current
    ScanIDCard(
        context = context,
        onScanResult = { scanResult ->
            // Handle the scan result here
        }
    )
}
