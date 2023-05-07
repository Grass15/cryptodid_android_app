package com.loginid.cryptodid.presentation.navigation.drawer_navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector


sealed class NavigationBodyItems(
    val icon: ImageVector,
    val title: String,
    val route: String,
    val contentDescription: String,
) {
    object DeletedVCs: NavigationBodyItems(
        icon = Icons.Filled.Delete,
        title = "View deleted VCs",
        route = "Deleted VCs screen",
        contentDescription = "Deleted items"
    )
    object PersonalInfos: NavigationBodyItems(
        icon = Icons.Filled.Person,
        title = "Update user profile",
        route = "Deleted VCs screen",
        contentDescription = "Presonal infos"
    )
    object Status: NavigationBodyItems(
        icon = Icons.Filled.Settings,
        title = "Settings",
        route = "Deleted VCs screen",
        contentDescription = "Cleann"
    )
}