package com.instavault.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.instavault.app.data.local.SessionExpiryNotifier
import com.instavault.app.ui.login.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var sessionExpiredMessage by rememberSaveable { mutableStateOf<String?>(null) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "login"

    LaunchedEffect(navController) {
        SessionExpiryNotifier.sessionExpired.collect { expired ->
            if (expired) {
                sessionExpiredMessage = "Session expired. Please login again."
                navController.navigate("login") {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
                SessionExpiryNotifier.markHandled()
            }
        }
    }

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
                    sessionExpiredMessage = sessionExpiredMessage,
                    onNavigateNext = {
                        sessionExpiredMessage = null
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
                com.instavault.app.ui.profile.ProfileScreen(
                    onLogout = {
                        sessionExpiredMessage = "You have been logged out."
                        navController.navigate("login") {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
