package mvp.model.repository.remote;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import mvp.model.entity.FlickrPhoto;
import mvp.model.mapping.PhotoEntityMapper;
import mvp.model.mapping.PhotoEntityMapperInterface;
import mvp.model.repository.GameDataSource;
import mvp.model.repository.model.PlayerScore;
import mvp.model.rest.NetworkCallback;
import mvp.model.rest.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */
@Singleton
public class GameRemoteDataSource implements GameDataSource {
    @Inject
    public GameRemoteDataSource() {
    }

    @Override
    public void getPhotos(final NetworkCallback networkCallback) {
        final PhotoEntityMapperInterface serviceMapper = new PhotoEntityMapper();
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

    @Override
    public List<PlayerScore> getTopScores() {
        throw new UnsupportedOperationException("Operation not supported");
    }

    @Override
    public void addTopScore(PlayerScore playerScore) {
        throw new UnsupportedOperationException("Operation not supported");
    }
}
