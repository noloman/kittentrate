package kittentrate.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Observable
import kittentrate.ui.score.PlayerScore

/**
 * Created by Manuel Lorenzo on 11/02/2018.
 */
@Dao
interface PlayerScoreDao {
    @Query("SELECT * from scores")
    fun getTopScores(): Observable<List<PlayerScore>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTopScore(playerScore: PlayerScore): Observable<Long>

}