package kittentrate.score

import android.arch.persistence.room.*


/**
 * Created by Manuel Lorenzo on 18/11/2017.
 */
@Dao
interface PlayerScoreDao {
    @Query("SELECT * from Score")
    fun getTopScores(): List<PlayerScore>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTopScore(playerScore: PlayerScore): Long

    @Delete
    fun deleteAllScores(vararg playerScore: PlayerScore)
}