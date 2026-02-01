package com.example.madca_whackamole.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import com.example.madca_whackamole.data.*
import com.example.madca_whackamole.ui.*
@Composable
fun GameScreen(
    db: androidx.room.RoomDatabase,
    currentUser: UserEntity,
    scoreDao: ScoreDao,
    onNavigateToSettings: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    gameDurationSeconds: Int = 30
) {
    val gridSize = 3
    val totalCells = gridSize * gridSize

    var moleIndex by remember { mutableIntStateOf(Random.nextInt(totalCells)) }
    var score by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(gameDurationSeconds) }
    var gameRunning by remember { mutableStateOf(true) }

    // Creating timer effect
    LaunchedEffect(gameRunning) {
        while (gameRunning && timeLeft > 0) {
            delay(1000)
            timeLeft -= 1
        }
        gameRunning = false
    }

    // Creating mole movement delay
    LaunchedEffect(gameRunning) {
        while (gameRunning) {
            delay(Random.nextLong(700, 1000))
            moleIndex = Random.nextInt(totalCells)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Wack-a-Mole", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Score: $score", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Time: $timeLeft s", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // 3x3 Mole Wacking Grid w/ score saving to database & mole shifting at intervals
        // I also made it more clear where the mole shows up , so only the button with a mole is
        // visible when it is required to be clicked (See in fun Molebutton)
        for (row in 0 until gridSize) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (col in 0 until gridSize) {
                    val index = row * gridSize + col
                    MoleButton(isMole = index == moleIndex) {
                        if (index == moleIndex && gameRunning) {
                            score += 1
                            CoroutineScope(Dispatchers.IO).launch {
                                scoreDao.insertScore(Score(userId = currentUser.userId, score = score))
                            }
                            moleIndex = Random.nextInt(totalCells)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons for Settings , Leaderboards , Restarts
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                score = 0
                timeLeft = gameDurationSeconds
                gameRunning = true
                moleIndex = Random.nextInt(totalCells)
            }) {
                Text("Restart")
            }

            Button(onClick = onNavigateToLeaderboard) {
                Text("Leaderboard")
            }

            Button(onClick = onNavigateToSettings) {
                Text("Settings")
            }
        }
    }
}

@Composable
fun MoleButton(isMole: Boolean, onClick: () -> Unit) {
    if (isMole) {
        Button(
            onClick = onClick,
            modifier = Modifier.size(80.dp)
        ) {
            Text("🐹")
        }
    } else {
        Button(
            onClick = {},
            modifier = Modifier.size(80.dp),
            enabled = false
        ) {}
    }
}





