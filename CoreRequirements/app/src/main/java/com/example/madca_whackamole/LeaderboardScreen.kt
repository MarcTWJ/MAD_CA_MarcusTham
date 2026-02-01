package com.example.madca_whackamole.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.madca_whackamole.data.ScoreDao
import com.example.madca_whackamole.data.UserBestScore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LeaderboardScreen(
    scoreDao: ScoreDao,
    currentUser : String,
    onBack: () -> Unit,
    scoreUpdatedTrigger: Int
) {
    var leaderboard by remember { mutableStateOf(listOf<UserBestScore>()) }
    var isLoading by remember { mutableStateOf(true) }


    LaunchedEffect(scoreUpdatedTrigger) {
        isLoading = true
        leaderboard = scoreDao.getLeaderboard()
        isLoading = false
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Leaderboard", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Text("Loading...")
        } else if (leaderboard.isEmpty()) {
            Text("No scores yet!")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(leaderboard) { userScore ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(userScore.username)
                            Text("${userScore.bestScore}")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}





