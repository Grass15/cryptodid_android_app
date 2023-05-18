package com.loginid.cryptodid.presentation.navigation.screens

sealed class IssuerScreen(
    val route: String
){
    object MicroBlinkScreenScreen: IssuerScreen("micro_blin_screen")
    object GovernmentIDVCScreen: IssuerScreen("government_id_vc_screen")
    object VCFromIssuerGateWayScreen: IssuerScreen("vc_from_issuer_gateway_screen")
    //other bottom screens

    //Temps
    object PLAIDVCScreen: IssuerScreen("plaid_vc_screen")
    object CreditScorevcScreen: IssuerScreen("credit_score_vc_screen")
}
