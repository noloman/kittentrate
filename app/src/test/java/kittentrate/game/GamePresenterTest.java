package kittentrate.game;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import kittentrate.data.repository.GameRepository;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by Manuel Lorenzo
 */
public class GamePresenterTest {
    @Mock
    private
    GameContract.View view;
    @Mock
    private
    GameRepository gameRepository;

    private GameContract.Presenter gamePresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        gamePresenter = new GamePresenter(gameRepository, view);
    }

    @Test
    public void testGamePresenter_viewShouldShowLoadingView_whenStarted() {
        gamePresenter.start();
        verify(view).showLoadingView();
    }

    @Test
    public void testGamePresenter_viewShouldRemoveViewFlipper_whenRemovingCardsFromMaps() {
        gamePresenter.removeCardsFromMaps();
        verify(view).removeViewFlipper();
    }

    @Test
    public void testGamePresenter_gameScoreChanged_shouldCheckEndOfGame() {
        gamePresenter.onGameScoreChanged(1);

        verify(view).onScoreChanged(eq(1));
        verify(view).checkGameFinished();
    }
}