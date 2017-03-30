package mvp.view.scores;

/**
 * Created by Manuel Lorenzo
 */

public interface ScoresContract {
    interface View {
        void showEmptyView();

        void showLoadingView();

        void hideLoadingView();

        void showScores();
    }

    interface Presenter {
        void start();
    }
}
