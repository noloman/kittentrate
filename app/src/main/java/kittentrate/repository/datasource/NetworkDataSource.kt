package kittentrate.repository.datasource

import io.reactivex.Observable
import kittentrate.data.model.FlickrPhoto

/**
 * Created by Manuel Lorenzo on 12/02/2018.
 */
interface NetworkDataSource {
    fun getPhotosWithSavedTag(): Observable<FlickrPhoto>
}