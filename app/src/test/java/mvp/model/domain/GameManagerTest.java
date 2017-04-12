package mvp.model.domain;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import mvp.model.entity.Card;
import mvp.model.entity.Game;
import mvp.view.game.GamePresenter;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Manuel Lorenzo
 */
public class GameManagerTest {
    @Mock
    private
    GamePresenter gamePresenter;
    @Mock
    private Card card1;
    @Mock
    private Card card2;
    @Mock
    private Game game;

    private GameManager gameManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        gameManager = new GameManager(gamePresenter);
    }

    @Test
    public void cardFlipped_whenTwoCardsWithDifferentId_shouldTurnCardsOver() throws Exception {
        when(card1.getId()).thenReturn("1");
        when(card2.getId()).thenReturn("2");
        gameManager.cardFlipped(0, card1);
        gameManager.cardFlipped(1, card2);

        verify(gamePresenter).onTurnCardsOver();
    }

    @Test
    public void cardFlipped_whenTwoCardsWithSameId_shouldRemoveThemFromAdapter_andIncreaseScore() throws Exception {
        when(card1.getId()).thenReturn("1");

        gameManager.cardFlipped(0, card1);
        gameManager.cardFlipped(1, card1);
        verify(gamePresenter, times(2)).removeViewFlipper();
        verify(gamePresenter, times(2)).notifyAdapterItemRemoved(anyString());
        verify(gamePresenter).onGameScoreChanged(anyInt());
    }

    @Test
    public void removeCardsFromMap() throws Exception {
        when(card1.getId()).thenReturn("1");
        gameManager.cardFlipped(0, card1);
        gameManager.removeCardsFromMap();

        verify(gamePresenter).notifyAdapterItemChanged(anyInt());
    }
}