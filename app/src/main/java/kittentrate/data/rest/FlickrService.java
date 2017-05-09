package kittentrate.data.rest;

import kittentrate.data.repository.model.FlickrPhoto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */

public interface FlickrService {
    @GET("/services/rest/")
        // TODO: Handle array of Strings
    Call<FlickrPhoto> getPhotos(@Query("tags") String photoTag);
}
