package com.loginid.cryptodid.presentation.navigation.graphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.loginid.cryptodid.presentation.home.main_home.components.HomeScreen
import com.loginid.cryptodid.presentation.navigation.screens.HomeScreen
import com.loginid.cryptodid.utils.Constants.HOME_ROOT_GRAPH

@RequiresApi(Build.VERSION_CODES.P)
fun NavGraphBuilder.homeGraph(
    navController: NavHostController
) {
    navigation(startDestination = HomeScreen.MainHomeScreen.route,route = HOME_ROOT_GRAPH){
        composable(route = HomeScreen.MainHomeScreen.route){
           HomeScreen(navController = navController)
        }
    }
}