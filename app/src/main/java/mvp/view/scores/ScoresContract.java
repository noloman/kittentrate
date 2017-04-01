package mvp.view.scores;

import java.util.List;

import mvp.model.repository.model.PlayerScore;

/**
 * Created by Manuel Lorenzo
 */

public interface ScoresContract {
    interface View {
        void showEmptyView();

        void showLoadingView();

        void hideLoadingView();

        void showScores(List<PlayerScore> data);
    }

    interface Presenter {
        void start();
    }
}
