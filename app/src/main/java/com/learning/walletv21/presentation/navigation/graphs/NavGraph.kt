package com.learning.walletv21.presentation.navigation.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.learning.walletv21.presentation.home.vc.CardSwiper
import com.learning.walletv21.presentation.loginid_welcome.WelcomeScreen
import com.learning.walletv21.presentation.navigation.screens.WelcomeScreen
import com.learning.walletv21.utils.Constants.AUTH_GRAPH
import com.learning.walletv21.utils.Constants.ROOT_GRAPH


@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(navController = navController,startDestination = startDestination,route = ROOT_GRAPH){
        composable(route = WelcomeScreen.Welcome.route){
            //Here goes the welcome screen
                 WelcomeScreen(navController = navController)
        }
      authNavGraph(navController = navController)
      homeGraph(navController = navController)
    }
}