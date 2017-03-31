package mvp.model.repository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import mvp.model.di.scope.Local;
import mvp.model.di.scope.Remote;
import mvp.model.repository.local.GameLocalDataSource;
import mvp.model.repository.model.PlayerScore;
import mvp.model.repository.remote.GameRemoteDataSource;
import mvp.model.rest.NetworkCallback;

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */
@Singleton
public class GameRepository implements GameDataSource {
    private GameDataSource remoteDataSource;
    private GameDataSource localDataSource;

    @Inject
    public GameRepository(@Local GameLocalDataSource gameLocalDataSource, @Remote GameRemoteDataSource gameRemoteDataSource) {
        remoteDataSource = gameRemoteDataSource;
        localDataSource = gameLocalDataSource;
    }

    @Override
    public void getPhotos(NetworkCallback networkCallback) {
        remoteDataSource.getPhotos(networkCallback);
    }

    @Override
    public List<PlayerScore> getTopScores() {
        return localDataSource.getTopScores();
    }

    @Override
    public void addTopScore(PlayerScore playerScore) {
        remoteDataSource.addTopScore(playerScore);
    }
}
