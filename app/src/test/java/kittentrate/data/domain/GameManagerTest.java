package kittentrate.data.domain;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import kittentrate.game.Card;
import kittentrate.game.GameManager;
import kittentrate.game.GamePresenter;

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
    private GamePresenter gamePresenter;
    @Mock
    private Card card1;
    @Mock
    private Card card2;

    private GameManager gameManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        gameManager = new GameManager(gamePresenter);
    }

    @Test
    public void testGameManager_gamePresenterShouldTurnCardsOver_whenCardsAreFlipped() throws Exception {
        when(card1.getId()).thenReturn("1");
        when(card2.getId()).thenReturn("2");
        gameManager.cardFlipped(0, card1);
        gameManager.cardFlipped(1, card2);

        verify(gamePresenter).onTurnCardsOver();
    }

    @Test
    public void testGameManager_gamePresenterShouldRemoveViewFlipperAndChangeScore_whenCardsAreMatched() throws Exception {
        when(card1.getId()).thenReturn("1");

        gameManager.cardFlipped(0, card1);
        gameManager.cardFlipped(1, card1);
        verify(gamePresenter, times(2)).removeViewFlipper();
        verify(gamePresenter, times(2)).notifyAdapterItemRemoved(anyString());
        verify(gamePresenter).onGameScoreChanged(anyInt());
    }

    @Test
    public void testGameManager_gamePresenterShouldNotifyItemsChanged_whenGameManagerFlipsCard() throws Exception {
        when(card1.getId()).thenReturn("1");
        gameManager.cardFlipped(0, card1);
        gameManager.removeCardsFromMap();

        verify(gamePresenter).notifyAdapterItemChanged(anyInt());
    }
}