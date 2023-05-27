package com.loginid.cryptodid.presentation.navigation.drawer_navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.loginid.cryptodid.presentation.navigation.screens.DetailsScreen


sealed class NavigationBodyItems(
    val icon: ImageVector,
    val title: String,
    val route: String,
    val contentDescription: String,
) {
    object DeletedVCs: NavigationBodyItems(
        icon = Icons.Filled.Delete,
        title = "View deleted VCs",
        route = DetailsScreen.DeletedVCsScreen.route,
        contentDescription = "Deleted items"
    )
    object PersonalInfos: NavigationBodyItems(
        icon = Icons.Filled.Person,
        title = "Update user profile",
        route = DetailsScreen.UpdateUserInfoScreen.route,
        contentDescription = "Presonal infos"
    )
    object Status: NavigationBodyItems(
        icon = Icons.Filled.Settings,
        title = "Settings",
        route = DetailsScreen.UpdateUserInfoScreen.route,
        contentDescription = "Cleann"
    )
}