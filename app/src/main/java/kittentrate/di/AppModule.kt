package kittentrate.di

import dagger.Module
import dagger.Provides
import kittentrate.GameApplication
import javax.inject.Singleton

@Module
class AppModule(private val gameApplication: GameApplication) {
    @Provides
    @Singleton
    fun provideApplicationContext(): GameApplication {
        return gameApplication
    }
}