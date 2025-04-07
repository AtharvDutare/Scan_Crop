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
import com.example.trysample.auth.AuthViewModel
import com.example.trysample.navigation.*
import com.example.trysample.ui.screens.*
import com.example.trysample.ui.theme.TrySampleTheme
import com.example.trysample.weatherpart.WeatherViewModel
import com.google.firebase.FirebaseApp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trysample.auth.AuthState

/**
 * MainActivity is the entry point of the application.
 * It handles the splash screen, authentication state, and main navigation.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install the splash screen before calling super.onCreate()
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        // Flag to control when to remove the splash screen
        var keepSplashScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }
        
        // Initialize the WeatherViewModel for weather data
        val weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        
        // Set up the Compose UI
        setContent {
            TrySampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // State variables for splash screen and authentication
                    var showSplash by remember { mutableStateOf(true) }
                    var isAuthenticated by remember { mutableStateOf(false) }
                    
                    // Show the splash screen if showSplash is true
                    if (showSplash) {
                        SplashScreen(
                            onSplashComplete = {
                                showSplash = false
                                keepSplashScreen = false
                            }
                        )
                    } else {
                        // Initialize the navigation controller
                        val navController = rememberNavController()
                        
                        // Set up the main navigation graph
                        NavHost(
                            navController = navController,
                            // Start with auth flow if not authenticated, otherwise start with main app
                            startDestination = if (isAuthenticated) MAIN_ROUTE else AUTH_ROUTE
                        ) {
                            // Authentication navigation graph
                            authNavigation(
                                navController = navController,
                                onAuthSuccess = {
                                    // Update authentication state and navigate to main app
                                    isAuthenticated = true
                                    navController.navigate(MAIN_ROUTE) {
                                        // Remove the auth graph from the back stack
                                        popUpTo(AUTH_ROUTE) { inclusive = true }
                                    }
                                }
                            )
                            
                            // Main app navigation graph
                            composable(MAIN_ROUTE) {
                                MainScreen(
                                    weatherViewModel = weatherViewModel,
                                    onSignOut = {
                                        // Handle sign out
                                        isAuthenticated = false
                                        navController.navigate(AUTH_ROUTE) {
                                            // Remove the main graph from the back stack
                                            popUpTo(MAIN_ROUTE) { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Constant for the main app route
 */
const val MAIN_ROUTE = "main"

/**
 * MainScreen composable that provides the main UI of the application.
 * It includes a bottom navigation bar and the main content area.
 * 
 * @param viewModel The WeatherViewModel that provides weather data to the app.
 * @param onSignOut Callback function that is invoked when the user signs out.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    weatherViewModel: WeatherViewModel,
    onSignOut: () -> Unit
) {
    // Get the AuthViewModel
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()
    
    // Handle authentication state changes
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.SignedOut -> {
                onSignOut()
            }
            else -> {} // Handle other states if needed
        }
    }
    
    // Initialize the navigation controller for the main app
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Define the bottom navigation items
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Weather,
        BottomNavItem.Scan,
        BottomNavItem.Stats,
        BottomNavItem.Profile
    )

    // Set up the main screen with a bottom navigation bar
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
            // Home screen
            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    navController = navController
                )
            }
            
            // Weather screen with weather data from the view model
            composable(BottomNavItem.Weather.route) {
                WeatherScreen(weatherViewModel)
            }
            
            // Scan screen (placeholder)
            composable(BottomNavItem.Scan.route) {
                // TODO: Implement ScanScreen
                Text("Scan Screen")
            }
            
            // Market screen (previously Stats screen)
            composable(BottomNavItem.Stats.route) {
                MarketScreen()
            }
            
            // Profile screen with sign out functionality
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(
                    onSignOut = {
                        authViewModel.signOut()
                    }
                )
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