package mvp.view.kittentrate;

import android.widget.ViewFlipper;

import java.util.List;

import mvp.model.entity.Card;
import mvp.model.entity.PhotoEntity;

/**
 * Created by Manuel Lorenzo on 21/03/2017.
 */

interface KittenContract {
    interface View {
        void showLoadingView();

        void hideLoadingView();

        void turnCardsOver();

        void notifyAdapterItemChanged(int pos);

        void notifyAdapterItemRemoved(String id);

        void onGameFinished();

        void onScoreIncreased(int score);

        void setAdapterData(List<PhotoEntity> photoEntityList);

        void removeViewFlipper();

        void showErrorView();
    }

    interface Presenter {
        void onItemClicked(int position, Card card, ViewFlipper viewFlipper);

        void removeCardsFromMaps();
    }
}
