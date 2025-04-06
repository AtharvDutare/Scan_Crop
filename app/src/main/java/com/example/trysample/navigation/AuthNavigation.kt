package com.example.trysample.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.trysample.ui.screens.auth.LoginScreen
import com.example.trysample.ui.screens.auth.SignUpScreen

const val AUTH_ROUTE = "auth"
const val LOGIN_ROUTE = "login"
const val SIGNUP_ROUTE = "signup"

fun NavGraphBuilder.authNavigation(
    navController: NavController,
    onAuthSuccess: () -> Unit
) {
    navigation(startDestination = LOGIN_ROUTE, route = AUTH_ROUTE) {
        composable(LOGIN_ROUTE) {
            LoginScreen(
                onLoginClick = { email, password ->
                    // TODO: Implement actual login logic
                    onAuthSuccess()
                },
                onSignUpClick = {
                    navController.navigate(SIGNUP_ROUTE)
                }
            )
        }
        
        composable(SIGNUP_ROUTE) {
            SignUpScreen(
                onSignUpClick = { fullName, email, password, confirmPassword ->
                    // TODO: Implement actual signup logic
                    onAuthSuccess()
                },
                onLoginClick = {
                    navController.navigateUp()
                }
            )
        }
    }
} 