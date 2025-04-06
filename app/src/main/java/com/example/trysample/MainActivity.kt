package com.example.trysample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.trysample.navigation.*
import com.example.trysample.ui.screens.*
import com.example.trysample.ui.theme.TrySampleTheme
import com.example.trysample.weatherpart.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        var keepSplashScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }
        
        val weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        
        setContent {
            TrySampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showSplash by remember { mutableStateOf(true) }
                    var isAuthenticated by remember { mutableStateOf(false) }
                    
                    if (showSplash) {
                        SplashScreen(
                            onSplashComplete = {
                                showSplash = false
                                keepSplashScreen = false
                            }
                        )
                    } else {
                        val navController = rememberNavController()
                        
                        NavHost(
                            navController = navController,
                            startDestination = if (isAuthenticated) MAIN_ROUTE else AUTH_ROUTE
                        ) {
                            // Auth Flow
                            authNavigation(
                                navController = navController,
                                onAuthSuccess = {
                                    isAuthenticated = true
                                    navController.navigate(MAIN_ROUTE) {
                                        popUpTo(AUTH_ROUTE) { inclusive = true }
                                    }
                                }
                            )
                            
                            // Main App Flow
                            composable(MAIN_ROUTE) {
                                MainScreen(weatherViewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

const val MAIN_ROUTE = "main"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: WeatherViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Weather,
        BottomNavItem.Scan,
        BottomNavItem.Stats,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen()
            }
            composable(BottomNavItem.Weather.route) {
                WeatherScreen(viewModel)
            }
            composable(BottomNavItem.Scan.route) {
                // TODO: Implement ScanScreen
                Text("Scan Screen")
            }
            composable(BottomNavItem.Stats.route) {
                MarketScreen()
            }
            composable(BottomNavItem.Profile.route) {
                ProfileScreen()
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    TrySampleTheme {
//        AgroScanApp()
//    }
//}