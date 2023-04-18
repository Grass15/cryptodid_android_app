package com.learning.walletv21.presentation.navigation.screens

sealed class AuthScreen(val route: String){
       object Login : AuthScreen("login_screen")
       object Register : AuthScreen("register_screen")
       object  ForgotPass : AuthScreen("forgot_password_screen")
}
