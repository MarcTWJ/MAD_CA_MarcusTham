package com.example.madca_whackamole

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.madca_whackamole.data.*
import com.example.madca_whackamole.ui.*
import com.example.madca_whackamole.ui.theme.MADCAWhackAMoleTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MADCAWhackAMoleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    // Room Database
                    val db = remember {
                        Room.databaseBuilder(
                            applicationContext,
                            AppDatabase::class.java,
                            "wackamole_db"
                        ).build()
                    }
                    val userDao: UserDao = db.userDao()
                    val scoreDao: ScoreDao = db.scoreDao()

                    // Navigation
                    val navController = rememberNavController()
                    val currentUserState = remember { mutableStateOf<UserEntity?>(null) }
                    val scoreUpdatedTrigger = remember { mutableStateOf(false) }

                    NavHost(navController = navController, startDestination = "login") {

                        // Login Screen
                        composable("login") {
                            LoginScreen(
                                userDao = userDao,
                                onLoginSuccess = { user ->
                                    // Save user in state
                                    currentUserState.value = user
                                    // Navigate to game
                                    navController.navigate("game") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // Game Screen
                        composable("game") {
                            val user = currentUserState.value
                            if (user != null) {
                                GameScreen(
                                    db = db,
                                    currentUser = user,
                                    scoreDao = scoreDao,
                                    onNavigateToSettings = { navController.navigate("settings") },
                                    onNavigateToLeaderboard = {
                                        scoreUpdatedTrigger.value = true // trigger refresh
                                        navController.navigate("leaderboard")
                                    }
                                )
                            } else {
                                // Optional: show a placeholder while user loads
                                androidx.compose.material3.Text("Loading user...")
                            }
                        }

                        // Settings Screen
                        composable("settings") {
                            SettingsScreen(
                                onBack = { navController.popBackStack() }
                            )
                        }

                        // Leaderboards Screen
                        composable("leaderboard") {
                            val user = currentUserState.value
                            if (user != null) {
                                LeaderboardScreen(
                                    scoreDao = scoreDao,
                                    onBack = {
                                        navController.popBackStack()
                                        scoreUpdatedTrigger.value = false // reset trigger when leaving
                                    },
                                    currentUser = user.username,
                                    scoreUpdatedTrigger =+ 1
                                )
                            } else {
                                androidx.compose.material3.Text("Loading leaderboard...")
                            }
                        }
                    }
                }
            }
        }
    }
}















