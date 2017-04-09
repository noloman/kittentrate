package mvp.model.repository;

import java.util.List;

import mvp.model.repository.model.PlayerScore;
import mvp.model.rest.NetworkCallback;

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */
public class GameRepository implements GameDataSource {
    private GameDataSource remoteDataSource;
    private GameDataSource localDataSource;
    private static GameRepository gameRepository;

    public static GameRepository getInstance(GameDataSource gameLocalDataSource, GameDataSource gameRemoteDataSource) {
        if (gameRepository == null) {
            gameRepository = new GameRepository(gameLocalDataSource, gameRemoteDataSource);
        }
        return gameRepository;
    }

    private GameRepository(GameDataSource gameLocalDataSource, GameDataSource gameRemoteDataSource) {
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
        localDataSource.addTopScore(playerScore);
    }
}
