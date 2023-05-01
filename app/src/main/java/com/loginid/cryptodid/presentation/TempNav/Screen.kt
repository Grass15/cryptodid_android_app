package com.loginid.cryptodid.presentation.TempNav

sealed class Screen(val route: String){
    object ClaimListScreen : Screen("claim_list_screen")
    object ClaimDetailScreen: Screen("claim_detail_screen")
}
