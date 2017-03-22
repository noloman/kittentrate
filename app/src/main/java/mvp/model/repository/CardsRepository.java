package mvp.model.repository;

import mvp.model.rest.NetworkCallback;

/**
 * Created by manu on 18/03/2017.
 */

public class CardsRepository implements CardsDataSource {
    private CardsDataSource carsRemoteDataSource;

    public CardsRepository(CardsDataSource remoteDataSource) {
        carsRemoteDataSource = remoteDataSource;
    }

    @Override
    public void getPhotos(NetworkCallback networkCallback) {
        carsRemoteDataSource.getPhotos(networkCallback);
    }
}
