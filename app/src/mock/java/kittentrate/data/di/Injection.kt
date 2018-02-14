package kittentrate.di

import android.app.Application
import android.support.annotation.NonNull
import kittentrate.GameApplication
import kittentrate.api.NetworkDataSourceImpl
import kittentrate.data.preferences.SharedPreferencesDataSourceImpl
import kittentrate.db.DatabaseDataSourceImpl
import kittentrate.db.PlayerScoreDao
import kittentrate.repository.Repository
import kittentrate.repository.datasource.DatabaseDataSource
import kittentrate.repository.datasource.NetworkDataSource

/**
 * Created by Manuel Lorenzo on 04/04/2017.
 */

object Injection {
    fun provideRepository(): Repository {
        return GameApplication.repository
    }

    fun provideSharedPreferencesDataSource(gameApplication: Application): SharedPreferencesDataSourceImpl {
        return SharedPreferencesDataSourceImpl(gameApplication)
    }

    fun provideDatabaseDataSource(playerScoreDao: PlayerScoreDao): DatabaseDataSource {
        return DatabaseDataSourceImpl(playerScoreDao)
    }

//    fun provideAddScoreUseCase(): AddScoreUseCaseImpl {
//        return AddScoreUseCaseImpl(providePlayerScoreDao())
//    }

    private fun providePlayerScoreDao(): PlayerScoreDao {
        return GameApplication.database.playerScoreDao()
    }

    fun provideNetworkDataSource(@NonNull gameApplication: GameApplication): NetworkDataSource {
        return NetworkDataSourceImpl(provideRepository())
    }

//    fun provideGetPhotosUseCase(@NonNull gameApplication: GameApplication): GetPhotosUseCase {
//        return GetPhotosUseCaseImpl(provideRepository(gameApplication))
//    }
}