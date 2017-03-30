package mvp.view.kittentrate;

import android.widget.ViewFlipper;

import java.util.List;

import mvp.model.domain.GameManager;
import mvp.model.entity.Card;
import mvp.model.entity.PhotoEntity;
import mvp.model.mapping.PhotoEntityMapper;
import mvp.model.mapping.PhotoEntityMapperInterface;
import mvp.model.repository.KittentrateRepository;
import mvp.model.repository.local.KittentrateLocalDataSource;
import mvp.model.repository.remote.KittentrateRemoteDataSource;
import mvp.model.rest.NetworkCallback;

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */

public class GamePresenter implements Contract.Presenter, NetworkCallback {
    private final Contract.View view;
    private KittentrateRepository cardsRepository;
    private GameManager gameManager;
    private int score;

    GamePresenter(Contract.View view) {
        // TODO DI
        this.view = view;
        this.gameManager = new GameManager(this);
        PhotoEntityMapperInterface serviceMapper = new PhotoEntityMapper();
        KittentrateRemoteDataSource remoteDataSource = new KittentrateRemoteDataSource(this, serviceMapper);
        KittentrateLocalDataSource localDataSource = new KittentrateLocalDataSource();
        cardsRepository = new KittentrateRepository(localDataSource, remoteDataSource);
    }

    void start() {
        view.showLoadingView();
        cardsRepository.getPhotos(this);
    }

    public boolean shouldDispatchTouchEvent() {
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
        score = gameScore;
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

    int getScore() {
        return score;
    }
}
