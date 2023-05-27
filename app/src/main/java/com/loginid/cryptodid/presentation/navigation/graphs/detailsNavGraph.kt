package com.loginid.cryptodid.presentation.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.loginid.cryptodid.presentation.details.userDetails.deletedVCsScreen
import com.loginid.cryptodid.presentation.details.userDetails.updateUserInfo
import com.loginid.cryptodid.presentation.navigation.screens.DetailsScreen
import com.loginid.cryptodid.utils.Constants

fun NavGraphBuilder.detailsGraph(
    navController: NavHostController
) {
    navigation(startDestination = DetailsScreen.UpdateUserInfoScreen.route,route = Constants.DETAILS_GRAPH){
        composable(route = DetailsScreen.UpdateUserInfoScreen.route){
                updateUserInfo()
        }
        composable(route = DetailsScreen.DeletedVCsScreen.route){
            deletedVCsScreen()
        }
    }
}