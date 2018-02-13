package kittentrate.di

import android.content.Context
import android.support.annotation.NonNull
import kittentrate.GameApplication
import kittentrate.api.NetworkDataSourceImpl
import kittentrate.data.preferences.SharedPreferencesDataSourceImpl
import kittentrate.db.DatabaseDataSourceImpl
import kittentrate.db.PlayerScoreDao
import kittentrate.repository.Repository
import kittentrate.repository.datasource.DatabaseDataSource
import kittentrate.repository.datasource.NetworkDataSource
import kittentrate.usecase.AddScoreUseCaseImpl
import kittentrate.usecase.GetPhotosUseCase
import kittentrate.usecase.GetPhotosUseCaseImpl

/**
 * Created by Manuel Lorenzo on 04/04/2017.
 */

object Injection {
    fun provideRepository(context: Context): Repository {
        return Repository.getInstance(provideNetworkDataSource(),
                provideDatabaseDataSource(providePlayerScoreDao()),
                provideSharedPreferencesDataSource(context))
    }

    fun provideSharedPreferencesDataSource(context: Context): SharedPreferencesDataSourceImpl {
        return SharedPreferencesDataSourceImpl(context)
    }

    fun provideDatabaseDataSource(playerScoreDao: PlayerScoreDao): DatabaseDataSource {
        return DatabaseDataSourceImpl(playerScoreDao)
    }

    fun provideAddScoreUseCase(@NonNull context: Context): AddScoreUseCaseImpl {
        return AddScoreUseCaseImpl(providePlayerScoreDao())
    }

    private fun providePlayerScoreDao(): PlayerScoreDao {
        return GameApplication.database.playerScoreDao()
    }

    fun provideNetworkDataSource(@NonNull context: Context): NetworkDataSource {
        return NetworkDataSourceImpl(provideGetPhotosUseCase(context.applicationContext))
    }

    fun provideGetPhotosUseCase(@NonNull context: Context): GetPhotosUseCase {
        return GetPhotosUseCaseImpl(provideSharedPreferencesDataSource(context.applicationContext))
    }
}