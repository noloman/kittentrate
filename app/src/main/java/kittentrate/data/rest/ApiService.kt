package kittentrate.data.rest

import io.reactivex.Observable
import kittentrate.data.repository.model.FlickrPhoto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */

interface ApiService {
    // TODO: Handle array of Strings
    @GET("/services/rest/")
    fun getPhotos(@Query("tags") photoTag: String): Observable<FlickrPhoto>
}
