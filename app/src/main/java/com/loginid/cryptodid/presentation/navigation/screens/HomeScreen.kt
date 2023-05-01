package com.loginid.cryptodid.presentation.navigation.screens

sealed class HomeScreen(
  val  route: String
) {
    object MainHomeScreen: HomeScreen("main_home_screen")
}