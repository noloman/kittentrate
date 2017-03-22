package mvp.model.repository;

import mvp.model.rest.NetworkCallback;

/**
 * Created by manu on 18/03/2017.
 */

public interface CardsDataSource {
    void getPhotos(NetworkCallback networkCallback);
}
