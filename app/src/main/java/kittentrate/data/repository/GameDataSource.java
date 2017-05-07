package kittentrate.data.repository;

import java.util.List;

import kittentrate.score.PlayerScore;
import kittentrate.data.rest.NetworkCallback;

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */

public interface GameDataSource {
    void getPhotos(NetworkCallback networkCallback);

    List<PlayerScore> getTopScores();

    long addTopScore(PlayerScore playerScore);
}
