package mvp.model.repository;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import mvp.model.repository.model.PlayerScore;

/**
 * Created by Manuel Lorenzo
 */

public class ScoresLoader extends AsyncTaskLoader<List<PlayerScore>> {
    private final GameRepository gameRepository;
    private List<PlayerScore> data;

    public ScoresLoader(Context context, GameRepository gameRepository) {
        super(context);
        this.gameRepository = gameRepository;
    }

    @Override
    public List<PlayerScore> loadInBackground() {
        return gameRepository.getTopScores();
    }

    @Override
    protected void onStartLoading() {
        if (data != null) {
            // Use cached data
            deliverResult(data);
        } else {
            // We have no data, so kick off loading it
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<PlayerScore> data) {
        if (isReset()) {
            return;
        }

        if (isStarted()) {
            this.data = data;
            super.deliverResult(this.data);
        }
    }
}
