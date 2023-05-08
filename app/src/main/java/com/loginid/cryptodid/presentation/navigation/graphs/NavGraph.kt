package com.loginid.cryptodid.presentation.navigation.graphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.loginid.cryptodid.presentation.loginid_welcome.WelcomeScreen
import com.loginid.cryptodid.presentation.navigation.screens.WelcomeScreen
import com.loginid.cryptodid.utils.Constants.ROOT_GRAPH


@RequiresApi(Build.VERSION_CODES.P)
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
        detailsGraph(navController = navController)
    }
}