package com.learning.walletv21.presentation.navigation.drawer_navigation

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
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
        title = "View deleted VCs",
        route = "Deleted VCs screen",
        contentDescription = "Presonal infos"
    )
    object Status: NavigationBodyItems(
        icon = Icons.Filled.Settings,
        title = "View deleted VCs",
        route = "Deleted VCs screen",
        contentDescription = "Cleann"
    )
}