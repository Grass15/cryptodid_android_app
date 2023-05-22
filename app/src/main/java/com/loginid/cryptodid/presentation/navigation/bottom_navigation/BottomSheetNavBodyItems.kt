package com.loginid.cryptodid.presentation.navigation.bottom_navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.loginid.cryptodid.presentation.navigation.screens.IssuerScreen

sealed class BottomSheetNavBodyItems(
    val icon: ImageVector,
    val title: String,
    val route: String,
    val contentDescription: String,
) {
    object GovVC: BottomSheetNavBodyItems(
        icon = Icons.Filled.DocumentScanner,
        title = "Get Government ID VC",
        route = IssuerScreen.GovernmentIDVCScreen.route,
        contentDescription = "Deleted items"
    )
    object IssuerGateWay: BottomSheetNavBodyItems(
        icon = Icons.Filled.DocumentScanner,
        title = "Get VC From Issuer Gateway",
        route = IssuerScreen.VCFromIssuerGateWayScreen.route,
        contentDescription = "Deleted items"
    )
    object QRIssuerGateWay: BottomSheetNavBodyItems(
        icon = Icons.Filled.DocumentScanner,
        title = "Get VC using QR Code",
        route = IssuerScreen.VCFromIssuerGateWayScreen.route,
        contentDescription = "Deleted items"
    )
    object BankVC: BottomSheetNavBodyItems(
        icon = Icons.Filled.Money,
        title = "Bank VC",
        route = IssuerScreen.PLAIDVCScreen.route,
        contentDescription = "Deleted items"
    )
    object CreditScoreVC: BottomSheetNavBodyItems(
        icon = Icons.Filled.Score,
        title = "Credit Score VC",
        route = IssuerScreen.CreditScorevcScreen.route,
        contentDescription = "Presonal infos"
    )
    object DriverLicenceVC: BottomSheetNavBodyItems(
        icon = Icons.Filled.CardMembership,
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
        icon = Icons.Filled.SimCard,
        title = "Telecom VC",
        route = "",
        contentDescription = "Cleann"
    )
    object BlinkVC: BottomSheetNavBodyItems(
        icon = Icons.Filled.Camera,
        title = "Blink VC",
        route = IssuerScreen.MicroBlinkScreenScreen.route,
        contentDescription = "Cleann"
    )

    object PrivilegeVC: BottomSheetNavBodyItems(
        icon = Icons.Filled.HowToVote,
        title = "Add Privilege",
        route = IssuerScreen.VotingScreen.route,
        contentDescription = "voting"
    )
    object SINVC: BottomSheetNavBodyItems(
        icon = Icons.Filled.CardMembership,
        title = "Get Insurance Number VC",
        route = IssuerScreen.GetInsuranceNumberScreen.route,
        contentDescription = "Access "
    )

}