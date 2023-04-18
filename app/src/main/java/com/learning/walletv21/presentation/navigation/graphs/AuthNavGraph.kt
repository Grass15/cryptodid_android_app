package com.learning.walletv21.presentation.navigation.graphs

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.learning.walletv21.presentation.authentication.login.LoginScreen
import com.learning.walletv21.presentation.authentication.login.LoginScreenViewModel
import com.learning.walletv21.presentation.authentication.register.RegisterScreen
import com.learning.walletv21.presentation.authentication.register.RegisterScreenViewModel
import com.learning.walletv21.presentation.navigation.screens.AuthScreen
import com.learning.walletv21.utils.Constants.AUTH_GRAPH


fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = AuthScreen.Login.route,
        route = AUTH_GRAPH
    ){
        composable(
            route = AuthScreen.Login.route
        ){
            val viewModel = hiltViewModel<LoginScreenViewModel>()
            val currentState by viewModel.state.collectAsState()
            LoginScreen(navController = navController, currentState = currentState, viewModel = viewModel)
        }

        composable(
            route = AuthScreen.Register.route
        ){
            val viewModel = hiltViewModel<RegisterScreenViewModel>()
            val currentState by viewModel.state.collectAsState()
             RegisterScreen(navController = navController,currentState = currentState, viewModel = viewModel)
        }
        composable(
            route = AuthScreen.ForgotPass.route
        ){

        }
    }
}