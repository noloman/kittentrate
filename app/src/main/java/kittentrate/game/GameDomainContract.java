package kittentrate.game;

/**
 * Created by Manuel Lorenzo
 */

public interface GameDomainContract {
    interface Manager {
        void cardFlipped(int position, Card card);

        void removeCardsFromMap();

        boolean shouldDispatchUiEvent();

        void resetScore();
    }
}
