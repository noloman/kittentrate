package kittentrate.game;

import android.widget.ViewFlipper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import kittentrate.data.repository.GameRepository;

import static org.mockito.Mockito.verify;

/**
 * Created by Manuel Lorenzo
 */
public class GamePresenterTest {
    @Mock
    private
    Card card;
    @Mock
    private
    ViewFlipper viewFlipper;
    @Mock
    private
    GameContract.View view;
    @Mock
    private
    GameManager gameManager;
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
    public void t1() {
        gamePresenter.start();

        verify(view).showLoadingView();
    }

    @Test
    public void onIemClicked() {
        gamePresenter.onItemClicked(0, card, viewFlipper);

        verify(gameManager).cardFlipped(0, card);

    }

    @Test
    public void removeCardsFromMaps() {
        gamePresenter.removeCardsFromMaps();
        verify(view).removeViewFlipper();
    }

}