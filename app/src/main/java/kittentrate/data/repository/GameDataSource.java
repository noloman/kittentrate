package kittentrate.data.repository;

import java.util.List;

import kittentrate.score.PlayerScore;
import kittentrate.data.rest.NetworkCallback;

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */

public interface GameDataSource {
    void getPhotos(final String photoTag, final NetworkCallback networkCallback);

    List<PlayerScore> getTopScores();

    long addTopScore(final PlayerScore playerScore);

    void deleteAllScores();
}
