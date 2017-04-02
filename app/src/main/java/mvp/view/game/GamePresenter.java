package mvp.view.game;

import android.widget.ViewFlipper;

import java.util.List;

import javax.inject.Inject;

import mvp.model.domain.GameDomainContract;
import mvp.model.domain.GameManager;
import mvp.model.entity.Card;
import mvp.model.entity.PhotoEntity;
import mvp.model.repository.GameRepository;
import mvp.model.repository.model.PlayerScore;
import mvp.model.rest.NetworkCallback;

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */

public class GamePresenter implements GameContract.Presenter, NetworkCallback {
    private final GameContract.View view;
    private GameRepository gameRepository;
    private int score;
    private GameDomainContract.Manager manager;

    @Inject
    GamePresenter(GameRepository gameRepository,GameContract.View view) {
        this.view = view;
        this.manager = new GameManager(this);
        this.gameRepository = gameRepository;

    }

    @Override
    public void start() {
        view.showLoadingView();
        gameRepository.getPhotos(this);
    }

    @Inject
    void setupListeners() {
        view.setPresenter(this);
    }

    @Override
    public void onScoredEntered(PlayerScore playerScore) {
        gameRepository.addTopScore(playerScore);
    }

    boolean shouldDispatchTouchEvent() {
        return manager.shouldDispatchUiEvent();
    }

    @Override
    public void onItemClicked(int position, Card card, ViewFlipper viewFlipper) {
        manager.cardFlipped(position, card);
    }

    @Override
    public void removeCardsFromMaps() {
        manager.removeCardsFromMap();
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

    @Override
    public void onGameScoreIncreased(int gameScore) {
        score = gameScore;
        view.onScoreIncreased(gameScore);
    }

    @Override
    public void onGameFinished() {
        view.onGameFinished();
    }

    @Override
    public void onTurnCardsOver() {
        view.turnCardsOver();
    }

    @Override
    public void notifyAdapterItemRemoved(String id) {
        view.notifyAdapterItemRemoved(id);
    }

    @Override
    public void removeViewFlipper() {
        view.removeViewFlipper();
    }

    @Override
    public void notifyAdapterItemChanged(Integer key) {
        view.notifyAdapterItemChanged(key);
    }

    int getScore() {
        return score;
    }
}
