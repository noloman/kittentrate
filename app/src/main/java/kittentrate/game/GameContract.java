package kittentrate.game;

import android.widget.ViewFlipper;

import java.util.List;

import kittentrate.data.repository.model.PhotoEntity;
import kittentrate.score.PlayerScore;

/**
 * Created by Manuel Lorenzo on 21/03/2017.
 */

public interface GameContract {
    interface View {
        void showLoadingView();

        void hideLoadingView();

        void turnCardsOver();

        void notifyAdapterItemChanged(int pos);

        void notifyAdapterItemRemoved(String id);

        void setAdapterData(List<PhotoEntity> photoEntityList);

        void removeViewFlipper();

        void showErrorView();

        boolean shouldDispatchTouchEvent();

        void onScoreChanged(int gameScore);

        void checkGameFinished();
    }

    interface Presenter {
        void onItemClicked(int position, Card card, ViewFlipper viewFlipper);

        void removeCardsFromMaps();

        void start();

        void onScoredEntered(PlayerScore playerScore);

        void notifyAdapterItemChanged(Integer key);

        void onTurnCardsOver();

        void removeViewFlipper();

        void onGameScoreChanged(int gameScore);

        void notifyAdapterItemRemoved(String id);

        void onKittensMenuItemClicked();

        void onPuppiesMenuItemClicked();
    }
}
