package com.example.madca_whackamole.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import com.example.madca_whackamole.data.Score

@Dao
interface ScoreDao {

    @Insert
    suspend fun insertScore(score: Score)

    @Query("SELECT * FROM scores WHERE userId = :userId ORDER BY score DESC LIMIT 1")
    suspend fun getPersonalBest(userId: Int): Score?

    @Query("SELECT * FROM scores ORDER BY score DESC")
    suspend fun getAllScores(): List<Score>

    @Query("""
    SELECT users.username AS username, MAX(scores.score) AS bestScore
    FROM scores
    INNER JOIN users ON scores.userId = users.userId
    GROUP BY users.username
    ORDER BY bestScore DESC
""")
    suspend fun getLeaderboard(): List<UserBestScore>
}



