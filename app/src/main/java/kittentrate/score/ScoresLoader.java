package kittentrate.score;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import io.reactivex.Observable;
import kittentrate.data.repository.GameRepository;

/**
 * Created by Manuel Lorenzo
 */

public class ScoresLoader extends AsyncTaskLoader<Observable<List<PlayerScore>>> {
    private final GameRepository gameRepository;
    private Observable<List<PlayerScore>> scoreList;

    ScoresLoader(Context context, GameRepository gameRepository) {
        super(context);
        this.gameRepository = gameRepository;
    }

    @Override
    public Observable<List<PlayerScore>> loadInBackground() {
        return gameRepository.getTopScores();
    }

    @Override
    protected void onStartLoading() {
        if (scoreList != null) {
            // Use cached scoreList
            deliverResult(scoreList);
        } else {
            // We have no scoreList, so kick off loading it
            forceLoad();
        }
    }

    @Override
    public void deliverResult(Observable<List<PlayerScore>> data) {
        if (isReset()) {
            return;
        }

        if (isStarted()) {
            scoreList = data;
            super.deliverResult(this.scoreList);
        }
    }
}
