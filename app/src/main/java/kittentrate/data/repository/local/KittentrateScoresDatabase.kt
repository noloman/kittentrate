package kittentrate.data.repository.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import kittentrate.score.PlayerScore
import kittentrate.score.PlayerScoreDao

/**
 * Created by Manuel Lorenzo on 21/11/2017.
 */
@Database(entities = arrayOf(PlayerScore::class), version = 1)
abstract class KittentrateScoresDatabase : RoomDatabase() {
    abstract fun playerScoresDao(): PlayerScoreDao
}