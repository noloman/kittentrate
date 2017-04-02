package mvp.model.domain;

import mvp.model.entity.Card;

/**
 * Created by Manuel Lorenzo
 */

public interface GameDomainContract {
    interface Manager {
        void cardFlipped(int position, Card card);

        void removeCardsFromMap();

        boolean shouldDispatchUiEvent();
    }
}
