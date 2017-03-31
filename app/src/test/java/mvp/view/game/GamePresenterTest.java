package mvp.view.game;

import android.widget.ViewFlipper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import mvp.model.entity.Card;
import mvp.model.repository.GameRepository;

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

    }

    @Test
    public void removeCardsFromMaps() {
        gamePresenter.removeCardsFromMaps();
        verify(view).removeViewFlipper();
    }

}