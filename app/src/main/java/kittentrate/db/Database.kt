package kittentrate.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import kittentrate.ui.score.PlayerScore

/**
 * Created by Manuel Lorenzo on 21/11/2017.
 */
@Database(entities = [(PlayerScore::class)], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun playerScoreDao(): PlayerScoreDao
}