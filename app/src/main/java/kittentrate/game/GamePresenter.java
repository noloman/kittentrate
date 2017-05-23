package kittentrate.game;

import android.widget.ViewFlipper;

import java.util.List;

import kittentrate.data.repository.GameRepository;
import kittentrate.data.repository.model.PhotoEntity;
import kittentrate.data.rest.NetworkCallback;
import kittentrate.score.PlayerScore;
import kittentrate.utils.Constants;

/**
 * Created by Manuel Lorenzo on 18/03/2017.
 */

public class GamePresenter implements GameContract.Presenter, NetworkCallback {
    private final GameContract.View view;
    private GameRepository repository;
    private int score;
    private GameDomainContract.Manager manager;

    GamePresenter(GameRepository repository, GameContract.View view) {
        this.view = view;
        this.manager = new GameManager(this);
        this.repository = repository;
    }

    @Override
    public void start() {
        view.showLoadingView();
        repository.getPhotos(this);
    }

    @Override
    public void onScoredEntered(PlayerScore playerScore) {
        if (repository.addTopScore(playerScore) == -1) {
            view.showErrorView();
        }
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
        view.showLoadingView();
        view.setAdapterData(photoEntityList);
        view.hideLoadingView();
    }

    @Override
    public void onFailure(Throwable error) {
        view.showErrorView();
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
    public void onKittensMenuItemClicked() {
        repository.setPreferencesPhotoTag(Constants.PHOTO_TAG_KITTEN_VALUE);
        repository.getPhotos(this);
        manager.resetScore();
        view.onScoreChanged(0);
    }

    @Override
    public void onPuppiesMenuItemClicked() {
        repository.setPreferencesPhotoTag(Constants.PHOTO_TAG_KITTEN_VALUE);
        repository.getPhotos(this);
        manager.resetScore();
        view.onScoreChanged(0);
    }

    @Override
    public void removeViewFlipper() {
        view.removeViewFlipper();
    }

    @Override
    public void onGameScoreChanged(int gameScore) {
        score = gameScore;
        view.onScoreChanged(gameScore);
        view.checkGameFinished();
    }

    @Override
    public void notifyAdapterItemChanged(Integer key) {
        view.notifyAdapterItemChanged(key);
    }

    int getScore() {
        return score;
    }
}
