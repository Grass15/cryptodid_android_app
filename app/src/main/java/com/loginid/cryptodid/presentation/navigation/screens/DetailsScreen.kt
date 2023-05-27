package com.loginid.cryptodid.presentation.navigation.screens

sealed class DetailsScreen(val route: String) {
    object UpdateUserInfoScreen : DetailsScreen("update_user_info_screen")
    object DeletedVCsScreen: DetailsScreen("deleted_vcs_screen")
    object SettingsScreen: DetailsScreen("settings_screen")
}