package mvp.model.repository.remote;

import mvp.model.entity.FlickrPhoto;
import mvp.model.mapping.PhotoEntityMapperInterface;
import mvp.model.repository.CardsDataSource;
import mvp.model.rest.NetworkCallback;
import mvp.model.rest.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by manu on 18/03/2017.
 */

public class CardsRemoteDataSource implements CardsDataSource {
    private final NetworkCallback networkCallback;
    private PhotoEntityMapperInterface serviceMapper;

    public CardsRemoteDataSource(NetworkCallback networkCallback, PhotoEntityMapperInterface serviceMapper) {
        this.serviceMapper = serviceMapper;
        this.networkCallback = networkCallback;
    }

    @Override
    public void getPhotos(final NetworkCallback networkCallback) {
        Call<FlickrPhoto> call = RetrofitClient.getRetrofitClient().getPhotos();
        call.enqueue(new Callback<FlickrPhoto>() {
            @Override
            public void onResponse(Call<FlickrPhoto> call, Response<FlickrPhoto> response) {
                networkCallback.onSuccess(serviceMapper.transform(response.body().getPhotos().getPhoto()));
            }

            @Override
            public void onFailure(Call<FlickrPhoto> call, Throwable t) {
                networkCallback.onFailure(t);
            }
        });
    }
}
