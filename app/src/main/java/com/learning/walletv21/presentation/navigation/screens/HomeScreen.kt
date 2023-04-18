package com.learning.walletv21.presentation.navigation.screens

sealed class HomeScreen(
  val  route: String
) {
    object MainHomeScreen: HomeScreen("main_home_screen")
}