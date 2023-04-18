package com.learning.walletv21.presentation.navigation.screens

sealed class WelcomeScreen(val route: String) {
     object Welcome : WelcomeScreen("welcome_screen")
}