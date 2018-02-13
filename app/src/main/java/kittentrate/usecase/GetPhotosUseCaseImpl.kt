package kittentrate.usecase

import io.reactivex.Observable
import kittentrate.api.RetrofitClient
import kittentrate.data.model.FlickrPhoto
import kittentrate.repository.datasource.SharedPreferencesDataSource

/**
 * Created by Manuel Lorenzo on 11/02/2018.
 */
class GetPhotosUseCaseImpl(private val sharedPreferencesDataSource: SharedPreferencesDataSource) :
        GetPhotosUseCase {
    override fun getSavedPhotoTag(): String {
        return sharedPreferencesDataSource.getPreferencesPhotoTag()
    }

    override fun getPhotosWithSavedTag(): Observable<FlickrPhoto> {
        return RetrofitClient.getRetrofitClient().getPhotos(getSavedPhotoTag())
    }
}