package kittentrate.data.repository.remote

import io.reactivex.Observable
import kittentrate.data.repository.GameDataSource
import kittentrate.data.repository.model.FlickrPhoto
import kittentrate.data.rest.RetrofitClient

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */
class GameRemoteDataSource private constructor() : GameDataSource.RemoteDataSource {

    override fun getPhotos(photoTag: String): Observable<FlickrPhoto> {
        return RetrofitClient.getRetrofitClient().getPhotos(photoTag)
    }

    companion object {
        private var gameRemoteDataSource: GameRemoteDataSource? = null

        val instance: GameRemoteDataSource
            get() {
                if (gameRemoteDataSource == null) {
                    gameRemoteDataSource = GameRemoteDataSource()
                }
                return gameRemoteDataSource as GameRemoteDataSource
            }
    }
}
