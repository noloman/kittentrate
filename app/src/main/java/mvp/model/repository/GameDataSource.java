package mvp.model.repository;

import java.util.List;

import mvp.model.repository.model.PlayerScore;
import mvp.model.rest.NetworkCallback;

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */

public interface GameDataSource {
    void getPhotos(NetworkCallback networkCallback);

    List<PlayerScore> getTopScores();

    void addTopScore(PlayerScore playerScore);
}
