package com.learning.walletv21.presentation.navigation.bottom_navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomSheetNavBodyItems(
    val icon: ImageVector,
    val title: String,
    val route: String,
    val contentDescription: String,
) {
    object BankVC: BottomSheetNavBodyItems(
        icon = Icons.Filled.Money,
        title = "Bank VC",
        route = "",
        contentDescription = "Deleted items"
    )
    object CreditScoreVC: BottomSheetNavBodyItems(
        icon = Icons.Filled.Score,
        title = "Credit Score VC",
        route = "",
        contentDescription = "Presonal infos"
    )
    object DriverLicenceVC: BottomSheetNavBodyItems(
        icon = Icons.Filled.TireRepair,
        title = "Driver Licence VC",
        route = "",
        contentDescription = "Cleann"
    )
    object PersenalVC: BottomSheetNavBodyItems(
        icon = Icons.Filled.DocumentScanner,
        title = "Persenal document VC",
        route = "",
        contentDescription = "Cleann"
    )
    object TelecomVC: BottomSheetNavBodyItems(
        icon = Icons.Filled.NetworkCell,
        title = "Telecom VC",
        route = "",
        contentDescription = "Cleann"
    )
}