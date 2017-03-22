package mvp.model.domain;

import java.util.Map;
import java.util.WeakHashMap;

import mvp.model.entity.Card;
import mvp.model.entity.Game;
import mvp.model.utils.Constants;
import mvp.view.kittentrate.KittenGamePresenter;

/**
 * Created by Manuel Lorenzo on 21/03/2017.
 */

public class GameManager {
    private Game game;
    private Map<Integer, Card> facingUpCardsWeakHashMap = new WeakHashMap<>(Constants.NUMBER_MATCHING_CARDS);
    private KittenGamePresenter kittenGamePresenter;

    public GameManager(KittenGamePresenter kittenGamePresenter) {
        this.kittenGamePresenter = kittenGamePresenter;
        this.game = new Game();
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

    public void cardFlipped(int position, Card card) {
        facingUpCardsWeakHashMap.put(position, card);
        if (facingUpCardsWeakHashMap.size() == Constants.NUMBER_MATCHING_CARDS) {
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
                kittenGamePresenter.removeViewFlipper();
                for (Map.Entry<Integer, Card> entry : facingUpCardsWeakHashMap.entrySet()) {
                    Card card1 = entry.getValue();
                    kittenGamePresenter.notifyAdapterItemRemoved(card1.getId());
                }
                facingUpCardsWeakHashMap.clear();
                increaseGameScore();
                kittenGamePresenter.onGameScoreIncreased(getGameScore());
                if (isGameFinished()) {
                    kittenGamePresenter.onGameFinished();
                }
                kittenGamePresenter.removeViewFlipper();
            } else {
                // Match not found; turn cards over and start from the beginning.
                kittenGamePresenter.onTurnCardsOver();
            }
        }
    }

    public void removeCardsFromMap() {
        for (Map.Entry<Integer, Card> entry : facingUpCardsWeakHashMap.entrySet()) {
            kittenGamePresenter.notifyAdapterItemChanged(entry.getKey());
        }
        facingUpCardsWeakHashMap.clear();
    }

    public boolean shouldDispatchUiEvent() {
        return facingUpCardsWeakHashMap.size() != Constants.NUMBER_MATCHING_CARDS;
    }
}
