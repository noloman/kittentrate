package kittentrate.di

import dagger.Module
import dagger.Provides
import kittentrate.GameApplication
import kittentrate.api.ApiService
import kittentrate.api.RetrofitClient
import kittentrate.data.preferences.SharedPreferencesDataSourceImpl
import kittentrate.data.viewmodel.factory.GameViewModelFactory
import kittentrate.db.Database
import kittentrate.db.PlayerScoreDao
import kittentrate.repository.Repository
import kittentrate.repository.datasource.SharedPreferencesDataSource
import kittentrate.ui.game.Game
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(flickrApi: ApiService,
                          playerScoreDao: PlayerScoreDao,
                          sharedPreferencesDataSource: SharedPreferencesDataSource): Repository {
        return Repository(flickrApi, playerScoreDao, sharedPreferencesDataSource)
    }

    @Provides
    @Singleton
    fun providePlayerScoreDao(database: Database): PlayerScoreDao {
        return database.playerScoreDao()
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return RetrofitClient.getRetrofitClient()
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesDataSource(gameApplication: GameApplication): SharedPreferencesDataSource {
        return SharedPreferencesDataSourceImpl(gameApplication)
    }

    @Provides
    @Singleton
    fun provideGameViewModelFactory(repository: Repository): GameViewModelFactory {
        return GameViewModelFactory(Game(), repository)
    }
}