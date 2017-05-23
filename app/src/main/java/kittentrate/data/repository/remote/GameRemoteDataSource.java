package kittentrate.data.repository.remote;

import kittentrate.data.mapping.PhotoEntityMapper;
import kittentrate.data.mapping.PhotoEntityMapperInterface;
import kittentrate.data.repository.GameDataSource;
import kittentrate.data.repository.model.FlickrPhoto;
import kittentrate.data.rest.NetworkCallback;
import kittentrate.data.rest.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */
public class GameRemoteDataSource implements GameDataSource.RemoteDataSource {
    private static GameRemoteDataSource gameRemoteDataSource;

    private GameRemoteDataSource() {
    }

    public static GameRemoteDataSource getInstance() {
        if (gameRemoteDataSource == null) {
            gameRemoteDataSource = new GameRemoteDataSource();
        }
        return gameRemoteDataSource;
    }

    @Override
    public void getPhotos(final String photoTag, final NetworkCallback networkCallback) {
        final PhotoEntityMapperInterface serviceMapper = new PhotoEntityMapper();
        Call<FlickrPhoto> call = RetrofitClient.getRetrofitClient().getPhotos(photoTag);
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
