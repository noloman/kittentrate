package mvp.view.kittentrate;

import android.widget.ViewFlipper;

import java.util.List;

import mvp.model.domain.GameManager;
import mvp.model.entity.Card;
import mvp.model.entity.PhotoEntity;
import mvp.model.mapping.PhotoEntityMapper;
import mvp.model.mapping.PhotoEntityMapperInterface;
import mvp.model.repository.CardsRepository;
import mvp.model.repository.remote.CardsRemoteDataSource;
import mvp.model.rest.NetworkCallback;

/**
 * Created by manu on 18/03/2017.
 */

public class KittenGamePresenter implements KittenContract.Presenter, NetworkCallback {
    private final KittenContract.View view;
    private CardsRepository cardsRepository;
    private GameManager gameManager;


    KittenGamePresenter(KittenContract.View view) {
        // TODO DI
        this.view = view;
        this.gameManager = new GameManager(this);
        PhotoEntityMapperInterface serviceMapper = new PhotoEntityMapper();
        CardsRemoteDataSource remoteDataSource = new CardsRemoteDataSource(this, serviceMapper);
        cardsRepository = new CardsRepository(remoteDataSource);
    }

    void start() {
        view.showLoadingView();
        cardsRepository.getPhotos(this);
    }

    boolean shouldDispatchTouchEvent() {
        return gameManager.shouldDispatchUiEvent();
    }

    @Override
    public void onItemClicked(int position, Card card, ViewFlipper viewFlipper) {
        gameManager.cardFlipped(position, card);
    }

    @Override
    public void removeCardsFromMaps() {
        gameManager.removeCardsFromMap();
        view.removeViewFlipper();
    }

    @Override
    public void onSuccess(List<PhotoEntity> photoEntityList) {
        view.setAdapterData(photoEntityList);
    }

    @Override
    public void onFailure(Throwable error) {
        view.showErrorView();
    }

    public void onGameScoreIncreased(int gameScore) {
        view.onScoreIncreased(gameScore);
    }

    public void onGameFinished() {
        view.onGameFinished();
    }

    public void onTurnCardsOver() {
        view.turnCardsOver();
    }

    public void notifyAdapterItemRemoved(String id) {
        view.notifyAdapterItemRemoved(id);
    }

    public void removeViewFlipper() {
        view.removeViewFlipper();
    }

    public void notifyAdapterItemChanged(Integer key) {
        view.notifyAdapterItemChanged(key);
    }
}
