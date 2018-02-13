package kittentrate.ui.score;

import java.util.List;

/**
 * Created by Manuel Lorenzo
 */

public interface ScoresContract {
    interface View {
        void showEmptyView();

        void showScores(List<PlayerScore> data);
    }

    interface Presenter {
        void start();
    }
}
