package com.example.madca_whackamole

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    onNavigateToSettings: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("wackamole_prefs", Context.MODE_PRIVATE)
    }

    var score by remember { mutableStateOf(0) }
    var highScore by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(30) }
    var moleIndex by remember { mutableStateOf(0) }
    var gameRunning by remember { mutableStateOf(false) }
    var hitIndex by remember { mutableStateOf<Int?>(null) }


    // Load high score
    LaunchedEffect(Unit) {
        highScore = prefs.getInt("HIGH_SCORE", 0)
    }

    // Countdown timer
    LaunchedEffect(gameRunning) {
        if (gameRunning) {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            gameRunning = false
        }
    }

    // Mole movement
    LaunchedEffect(gameRunning) {
        while (gameRunning) {
            delay(Random.nextLong(700, 1000))
            moleIndex = Random.nextInt(9)
        }
    }

    // Saving high score on game end
    LaunchedEffect(gameRunning) {
        if (!gameRunning && timeLeft == 0 && score > highScore) {
            highScore = score
            prefs.edit().putInt("HIGH_SCORE", highScore).apply()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Wack-a-Mole") },
            actions = {
                IconButton(onClick = onNavigateToSettings) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
            }
        )

        Column(modifier = Modifier.padding(16.dp)) {

            Text("Score: $score")
            Text("High Score: $highScore")
            Text("Time: $timeLeft")

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                score = 0
                timeLeft = 30
                gameRunning = true
            }) {
                Text(if (gameRunning) "Restart" else "Start")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3x3 Grid to whack moles on (Using Row)
            for (row in 0..2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (col in 0..2) {
                        val index = row * 3 + col
                        Button(
                            onClick = {
                                if (gameRunning && index == moleIndex) {
                                    score++
                                    hitIndex == index
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        ) {
                            if (gameRunning && index == moleIndex) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("🐹")
                                }
                            }

                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            LaunchedEffect(hitIndex) {
                if (hitIndex != null) {
                    delay(300)
                    hitIndex = null
                }
            }

            if (!gameRunning && timeLeft == 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Game Over! Final Score: $score")
            }
        }
    }
}
