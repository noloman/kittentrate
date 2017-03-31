package mvp.view.game;

import android.widget.ViewFlipper;

import java.util.List;

import javax.inject.Inject;

import mvp.model.domain.GameManager;
import mvp.model.entity.Card;
import mvp.model.entity.PhotoEntity;
import mvp.model.mapping.PhotoEntityMapper;
import mvp.model.mapping.PhotoEntityMapperInterface;
import mvp.model.repository.GameRepository;
import mvp.model.rest.NetworkCallback;

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */

public class GamePresenter implements GameContract.Presenter, NetworkCallback {
    private final GameContract.View view;
    private GameRepository gameRepository;
    private GameManager gameManager;
    private int score;

    @Inject
    GamePresenter(GameRepository gameRepository, GameContract.View view) {
        // TODO DI
        this.view = view;
        this.gameManager = new GameManager(this);
        PhotoEntityMapperInterface serviceMapper = new PhotoEntityMapper();
        //GameRemoteDataSource gameRemoteDataSource = new GameRemoteDataSource(this, serviceMapper);
        //GameLocalDataSource gameLocalDataSource = new GameLocalDataSource();
        //gameRepository = new GameRepository(gameLocalDataSource, gameRemoteDataSource);
        this.gameRepository = gameRepository;
    }

    @Override
    public void start() {
        view.showLoadingView();
        gameRepository.getPhotos(this);
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
