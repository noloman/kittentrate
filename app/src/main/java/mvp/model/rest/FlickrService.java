package mvp.model.rest;

import mvp.model.entity.FlickrPhoto;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by manu on 18/03/2017.
 */

public interface FlickrService {
    @GET("/services/rest/")
    Call<FlickrPhoto> getPhotos();
}
