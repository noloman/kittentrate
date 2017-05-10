package kittentrate.data.repository;

import java.util.List;

import kittentrate.score.PlayerScore;
import kittentrate.data.rest.NetworkCallback;

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
    public long addTopScore(PlayerScore playerScore) {
        return localDataSource.addTopScore(playerScore);
    }

    @Override
    public void deleteAllScores() {
        localDataSource.deleteAllScores();
    }
}
