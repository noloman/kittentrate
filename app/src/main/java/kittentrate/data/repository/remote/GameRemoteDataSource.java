package kittentrate.data.repository.remote;

import java.util.List;

import kittentrate.data.repository.model.FlickrPhoto;
import kittentrate.data.mapping.PhotoEntityMapper;
import kittentrate.data.mapping.PhotoEntityMapperInterface;
import kittentrate.data.repository.GameDataSource;
import kittentrate.score.PlayerScore;
import kittentrate.data.rest.NetworkCallback;
import kittentrate.data.rest.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */
public class GameRemoteDataSource implements GameDataSource {
    private static GameRemoteDataSource gameRemoteDataSource;

    private GameRemoteDataSource() {
    }

    @Override
    public void getPhotos(String photoTag, final NetworkCallback networkCallback) {
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

    @Override
    public List<PlayerScore> getTopScores() {
        throw new UnsupportedOperationException("Operation not supported");
    }

    @Override
    public long addTopScore(PlayerScore playerScore) {
        throw new UnsupportedOperationException("Operation not supported");
    }

    @Override
    public void deleteAllScores() {
        throw new UnsupportedOperationException("Operation not supported");
    }

    public static GameRemoteDataSource getInstance() {
        if (gameRemoteDataSource == null) {
            gameRemoteDataSource = new GameRemoteDataSource();
        }
        return gameRemoteDataSource;
    }
}
