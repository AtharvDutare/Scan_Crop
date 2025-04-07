package com.example.trysample.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object Weather : BottomNavItem(
        route = "weather",
        title = "Weather",
        icon = Icons.Default.WbSunny
    )
    object Agent : BottomNavItem(
        route = "agent",
        title = "Agent",
        icon = Icons.Default.SmartToy
    )
    object Stats : BottomNavItem(
        route = "stats",
        title = "market",
        icon = Icons.Default.BarChart
    )
    object Profile : BottomNavItem(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )
} 