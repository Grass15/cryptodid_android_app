package com.learning.walletv21.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.learning.walletv21.presentation.TempNav.Screen
import com.learning.walletv21.presentation.claim_details.components.ClaimDetailScreen
import com.learning.walletv21.presentation.claim_list.components.ClaimListScreen
import com.learning.walletv21.presentation.loginid_welcome.SplashViewModel
import com.learning.walletv21.presentation.navigation.graphs.SetupNavGraph
import com.learning.walletv21.presentation.theme.WalletV21Theme
import com.learning.walletv21.utils.Constants.AUTH_GRAPH
import com.learning.walletv21.utils.Constants.HOME_ROOT_GRAPH
import com.learning.walletv21.utils.Constants.PARAM_CLAIM_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@ExperimentalAnimationApi
@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController

    @Inject
    lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition{
            !splashViewModel.isLoading.value
        }
        setContent {
            WalletV21Theme {
                val screen by splashViewModel.startDestination

                navController = rememberNavController()
                // SetupNavGraph(navController = navController)
               /* NavHost(navController = navController, startDestination =  Screen.ClaimListScreen.route){
                    composable(
                        route = Screen.ClaimListScreen.route
                    ){
                        ClaimListScreen(navController = navController)
                    }
                    composable(
                        route = Screen.ClaimDetailScreen.route + "/{$PARAM_CLAIM_ID}"
                    ){
                        ClaimDetailScreen()
                    }
                }
                */
                SetupNavGraph(navController = navController, startDestination = AUTH_GRAPH/*HOME_ROOT_GRAPH*//*screen*/)
            }
        }
    }
}

