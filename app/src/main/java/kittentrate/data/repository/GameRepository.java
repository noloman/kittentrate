package kittentrate.data.repository;

import java.util.List;

import kittentrate.data.rest.NetworkCallback;
import kittentrate.score.PlayerScore;

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */
public class GameRepository implements GameDataSource.LocalDataSource, GameDataSource.SharedPreferencesDataSource, GameDataSource.Repository {
    private GameDataSource.RemoteDataSource remoteDataSource;
    private GameDataSource.LocalDataSource localDataSource;
    private GameDataSource.SharedPreferencesDataSource preferencesManager;
    private static GameRepository gameRepository;

    public static GameRepository getInstance(GameDataSource.LocalDataSource gameLocalDataSource, GameDataSource.RemoteDataSource gameRemoteDataSource, GameDataSource.SharedPreferencesDataSource manager) {
        if (gameRepository == null) {
            gameRepository = new GameRepository(gameLocalDataSource, gameRemoteDataSource, manager);
        }
        return gameRepository;
    }

    private GameRepository(GameDataSource.LocalDataSource gameLocalDataSource, GameDataSource.RemoteDataSource gameRemoteDataSource, GameDataSource.SharedPreferencesDataSource manager) {
        remoteDataSource = gameRemoteDataSource;
        localDataSource = gameLocalDataSource;
        preferencesManager = manager;
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

    @Override
    public void setPreferencesPhotoTag(String photoTag) {
        preferencesManager.setPreferencesPhotoTag(photoTag);
    }

    @Override
    public String getPreferencesPhotoTag() {
        return preferencesManager.getPreferencesPhotoTag();
    }

    @Override
    public void getPhotos(NetworkCallback networkCallback) {
        String photoTag = preferencesManager.getPreferencesPhotoTag();
        remoteDataSource.getPhotos(photoTag, networkCallback);
    }
}
