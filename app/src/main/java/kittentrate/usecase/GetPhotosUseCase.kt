package kittentrate.usecase

import io.reactivex.Observable
import kittentrate.data.model.FlickrPhoto

/**
 * Created by Manuel Lorenzo on 12/02/2018.
 */
interface GetPhotosUseCase {
    fun getSavedPhotoTag(): String
    fun getPhotosWithSavedTag(): Observable<FlickrPhoto>
}