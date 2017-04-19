package kittentrate.score;

import java.util.List;

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
