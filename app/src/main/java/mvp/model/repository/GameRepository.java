package mvp.model.repository;

import java.util.List;

import mvp.model.mapping.PhotoEntityMapperInterface;
import mvp.model.repository.local.GameLocalDataSource;
import mvp.model.repository.model.PlayerScore;
import mvp.model.repository.remote.GameRemoteDataSource;
import mvp.model.rest.NetworkCallback;

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */

public class GameRepository implements KittentrateDataSource {
    private KittentrateDataSource carsRemoteDataSource;
    private KittentrateDataSource cardsLocalDataSource;

    public GameRepository(NetworkCallback networkCallback, PhotoEntityMapperInterface entityMapperInterface) {
        carsRemoteDataSource = new GameLocalDataSource();
        cardsLocalDataSource = new GameRemoteDataSource(networkCallback, entityMapperInterface);
    }

    @Override
    public void getPhotos(NetworkCallback networkCallback) {
        carsRemoteDataSource.getPhotos(networkCallback);
    }

    @Override
    public List<PlayerScore> getTopScores() {
        return cardsLocalDataSource.getTopScores();
    }

    @Override
    public void addTopScore(PlayerScore playerScore) {
        carsRemoteDataSource.addTopScore(playerScore);
    }
}
