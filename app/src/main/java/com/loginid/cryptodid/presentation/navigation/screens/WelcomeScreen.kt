package com.loginid.cryptodid.presentation.navigation.screens

sealed class WelcomeScreen(val route: String) {
     object Welcome : WelcomeScreen("welcome_screen")
}