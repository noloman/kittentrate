package kittentrate.di

import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import kittentrate.GameApplication
import kittentrate.db.Database
import javax.inject.Singleton

@Module
class DatabaseModule(gameApplication: GameApplication) {
    val database = Room.databaseBuilder(gameApplication, Database::class.java, "Database")
            .build()

    @Singleton
    @Provides
    fun providesDatabase(): Database {
        return database
    }
}