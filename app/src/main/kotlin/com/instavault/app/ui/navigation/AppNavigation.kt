package com.instavault.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.instavault.app.ui.login.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "login"

    androidx.compose.material3.Scaffold(
        bottomBar = {
            if (currentRoute != "login") {
                com.instavault.app.ui.home.VaultBottomNavigation(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = androidx.compose.ui.Modifier.padding(paddingValues)
        ) {
            composable("login") {
                LoginScreen(
                    onNavigateNext = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("home") {
                com.instavault.app.ui.home.HomeScreen(
                    onNavigateToTasks = { navController.navigate("tasks") },
                    onNavigateToGames = { navController.navigate("games") },
                    onNavigateToSpin = { navController.navigate("spin") }
                )
            }
            composable("tasks") {
                com.instavault.app.ui.tasks.TasksScreen()
            }
            composable("games") {
                com.instavault.app.ui.game.GameScreen(
                    onNavigateToGame = { gameId ->
                        // Placeholder navigation logic for individual games
                    }
                )
            }
            composable("profile") {
                // Placeholder
            }
        }
    }
}
