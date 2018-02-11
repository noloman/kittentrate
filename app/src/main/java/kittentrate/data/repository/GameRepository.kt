package kittentrate.data.repository

import io.reactivex.Observable
import kittentrate.GameApplication
import kittentrate.data.mapping.PhotoEntityMapper
import kittentrate.data.repository.model.FlickrPhoto
import kittentrate.data.repository.model.PhotoEntity
import kittentrate.score.PlayerScore
import kittentrate.utils.applySchedulers

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */
class GameRepository private constructor(private val localDataSource: GameDataSource.LocalDataSource,
                                         private val remoteDataSource: GameDataSource.RemoteDataSource,
                                         private val preferencesManager: GameDataSource.SharedPreferencesDataSource)
    : GameDataSource.LocalDataSource, GameDataSource.SharedPreferencesDataSource, GameDataSource.Repository {

    override fun getTopScores(): Observable<List<PlayerScore>> {
        return Observable
                .fromCallable({
                    GameApplication.database
                            .playerScoresDao().getTopScores()
                })
                .applySchedulers()
    }

    override fun addTopScore(playerScore: PlayerScore): Long =
            GameApplication.database.playerScoresDao()
                    .addTopScore(playerScore)

    override fun deleteAllScores() {
        localDataSource.deleteAllScores()
    }

    override fun setPreferencesPhotoTag(photoTag: String) {
        preferencesManager.preferencesPhotoTag = photoTag
    }

    override fun getPreferencesPhotoTag(): String = preferencesManager.preferencesPhotoTag

    override fun getPhotos(): Observable<List<PhotoEntity>> {
        val serviceMapper = PhotoEntityMapper()
        val photoTag = preferencesManager.preferencesPhotoTag
        return remoteDataSource.
                getPhotos(photoTag)
                .flatMap({ t: FlickrPhoto -> Observable.just(serviceMapper.transform(t.photos.photo)) })
                .applySchedulers()
    }

    companion object {
        private var gameRepository: GameRepository? = null

        fun getInstance(gameLocalDataSource: GameDataSource.LocalDataSource,
                        gameRemoteDataSource: GameDataSource.RemoteDataSource,
                        sharedPreferencesDataSource: GameDataSource.SharedPreferencesDataSource): GameRepository {
            if (gameRepository == null) {
                gameRepository = GameRepository(gameLocalDataSource, gameRemoteDataSource, sharedPreferencesDataSource)
            }
            return gameRepository as GameRepository
        }

        fun destroy() {
            gameRepository = null
        }
    }
}
