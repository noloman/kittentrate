package mvp.model.domain;

import java.util.Map;
import java.util.WeakHashMap;

import mvp.model.entity.Card;
import mvp.model.entity.Game;
import mvp.model.utils.Constants;
import mvp.view.game.GameContract;

/**
 * Created by Manuel Lorenzo on 21/03/2017.
 */

public class GameManager implements GameDomainContract.Manager {
    private Map<Integer, Card> facingUpCardsWeakHashMap = new WeakHashMap<>(Constants.NUMBER_MATCHING_CARDS);
    private GameContract.Presenter gamePresenter;
    private Game game;

    public GameManager(GameContract.Presenter gamePresenter) {
        this.gamePresenter = gamePresenter;
        this.game = new Game();
    }

    @Override
    public void cardFlipped(int position, Card card) {
        if (!facingUpCardsWeakHashMap.containsKey(position)) {
            facingUpCardsWeakHashMap.put(position, card);
            if (shouldCheckForCardsMatch()) {
                // This would be the last card, we should check for matches.
                String id = card.getId();
                boolean matchFound = true;
                for (Map.Entry<Integer, Card> entry : facingUpCardsWeakHashMap.entrySet()) {
                    Card loopCard = entry.getValue();
                    if (!loopCard.getId().equals(id)) {
                        matchFound = false;
                    }
                }
                if (matchFound) {
                    // Match found; remove cards from dataset
                    gamePresenter.removeViewFlipper();
                    for (Map.Entry<Integer, Card> entry : facingUpCardsWeakHashMap.entrySet()) {
                        Card card1 = entry.getValue();
                        gamePresenter.notifyAdapterItemRemoved(card1.getId());
                    }
                    facingUpCardsWeakHashMap.clear();
                    increaseGameScore();
                    gamePresenter.onGameScoreIncreased(getGameScore());
                    if (isGameFinished()) {
                        gamePresenter.onGameFinished();
                    }
                    gamePresenter.removeViewFlipper();
                } else {
                    // Match not found; turn cards over and start from the beginning.
                    gamePresenter.onTurnCardsOver();
                }
            }
        }
    }

    @Override
    public void removeCardsFromMap() {
        for (Map.Entry<Integer, Card> entry : facingUpCardsWeakHashMap.entrySet()) {
            gamePresenter.notifyAdapterItemChanged(entry.getKey());
        }
        facingUpCardsWeakHashMap.clear();
    }

    @Override
    public boolean shouldDispatchUiEvent() {
        return facingUpCardsWeakHashMap.size() != Constants.NUMBER_MATCHING_CARDS;
    }

    private boolean shouldCheckForCardsMatch() {
        return facingUpCardsWeakHashMap.size() == Constants.NUMBER_MATCHING_CARDS;
    }

    private boolean isGameFinished() {
        return game.getScore() == Integer.valueOf(Constants.CARDS_PER_PAGE) / Constants.NUMBER_MATCHING_CARDS;
    }

    private void increaseGameScore() {
        game.setScore(game.getScore() + 1);
    }

    private int getGameScore() {
        return game.getScore();
    }
}
