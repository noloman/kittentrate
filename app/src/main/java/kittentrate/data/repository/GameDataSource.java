package kittentrate.data.repository;

import java.util.List;

import io.reactivex.Observable;
import kittentrate.data.repository.model.FlickrPhoto;
import kittentrate.data.repository.model.PhotoEntity;
import kittentrate.score.PlayerScore;

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */

public interface GameDataSource {
    interface Repository {
        Observable<List<PhotoEntity>> getPhotos();
    }

    interface LocalDataSource {
        Observable<List<PlayerScore>> getTopScores();

        long addTopScore(final PlayerScore playerScore);

        void deleteAllScores();
    }

    interface RemoteDataSource {
        Observable<FlickrPhoto> getPhotos(final String photoTag);
    }

    interface SharedPreferencesDataSource {

        String getPreferencesPhotoTag();

        void setPreferencesPhotoTag(String photoTag);
    }
}
