package mvp.model.repository;

import java.util.List;

import mvp.model.repository.local.KittentrateLocalDataSource;
import mvp.model.repository.model.PlayerScore;
import mvp.model.rest.NetworkCallback;

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */

public class KittentrateRepository implements KittentrateDataSource {
    private KittentrateDataSource carsRemoteDataSource;
    private KittentrateDataSource cardsLocalDataSource;

    public KittentrateRepository(KittentrateLocalDataSource localDataSource, KittentrateDataSource remoteDataSource) {
        carsRemoteDataSource = remoteDataSource;
        cardsLocalDataSource = localDataSource;
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
