package kittentrate.api

import io.reactivex.Observable
import kittentrate.data.model.FlickrPhoto
import kittentrate.repository.datasource.NetworkDataSource
import kittentrate.usecase.GetPhotosUseCase

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */
class NetworkDataSourceImpl(private val getPhotosUseCase: GetPhotosUseCase) : NetworkDataSource {
    override fun getPhotosWithSavedTag(): Observable<FlickrPhoto> {
        return getPhotosUseCase.getPhotosWithSavedTag()
    }
}
