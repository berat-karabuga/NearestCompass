package com.stargazer.nearestcompass.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.stargazer.nearestcompass.ui.screens.CompassScreen
import com.stargazer.nearestcompass.ui.screens.WelcomeScreen

@Composable
fun NavGraph0(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route // İlk ekran Welcome
    ) {
        composable(route = Screen.Welcome.route) {
            WelcomeScreen(
                onNavigateToCompass = {
                    navController.navigate(Screen.Compass.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Compass.route) {
           CompassScreen()
        }
    }
}