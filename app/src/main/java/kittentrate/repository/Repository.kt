package kittentrate.repository

import io.reactivex.Observable
import kittentrate.data.model.FlickrPhoto
import kittentrate.repository.datasource.DatabaseDataSource
import kittentrate.repository.datasource.NetworkDataSource
import kittentrate.repository.datasource.SharedPreferencesDataSource
import kittentrate.ui.score.PlayerScore
import kittentrate.utils.applySchedulers

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */
class Repository private constructor(private val networkDataSource: NetworkDataSource,
                                     private val databaseDataSource: DatabaseDataSource,
                                     private val sharedPreferencesDataSource: SharedPreferencesDataSource) :
        NetworkDataSource, DatabaseDataSource, SharedPreferencesDataSource {

    override fun getTopScores(): Observable<List<PlayerScore>> {
        return databaseDataSource.getTopScores()
    }

    override fun addTopScore(playerScore: PlayerScore): Observable<Long> {
        return databaseDataSource.addTopScore(playerScore)
    }

    override fun setPreferencesPhotoTag(photoTag: String) {
        sharedPreferencesDataSource.setPreferencesPhotoTag(photoTag)
    }

    override fun getPreferencesPhotoTag(): String = sharedPreferencesDataSource.getPreferencesPhotoTag()

    override fun getPhotosWithSavedTag(): Observable<FlickrPhoto> {
        return networkDataSource
                .getPhotosWithSavedTag()
                .applySchedulers()
    }

    companion object {
        private var repository: Repository? = null

        fun getInstance(networkDataSource: NetworkDataSource,
                        databaseDataSource: DatabaseDataSource,
                        sharedSharedPreferencesDataSource: SharedPreferencesDataSource):
                Repository {
            if (repository == null) {
                repository = Repository(networkDataSource,
                        databaseDataSource,
                        sharedSharedPreferencesDataSource)
            }
            return repository as Repository
        }

        fun destroy() {
            repository = null
        }
    }
}
