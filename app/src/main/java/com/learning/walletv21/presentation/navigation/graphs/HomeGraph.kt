package com.learning.walletv21.presentation.navigation.graphs

import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.learning.walletv21.presentation.TempNav.Screen
import com.learning.walletv21.presentation.home.main_home.components.HomeScreen
import com.learning.walletv21.presentation.home.vc.CardSwiper
import com.learning.walletv21.presentation.navigation.screens.HomeScreen
import com.learning.walletv21.utils.Constants.HOME_ROOT_GRAPH

@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.homeGraph(
    navController: NavHostController
) {
    navigation(startDestination = HomeScreen.MainHomeScreen.route,route = HOME_ROOT_GRAPH){
        composable(route = HomeScreen.MainHomeScreen.route){
           HomeScreen(navController = navController)

        }

    }
}