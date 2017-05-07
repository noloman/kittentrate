package kittentrate.scores;

import android.support.v4.app.LoaderManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import kittentrate.score.PlayerScore;
import kittentrate.score.ScoresContract;
import kittentrate.score.ScoresLoader;
import kittentrate.score.ScoresPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by Manuel Lorenzo
 */
public class ScoresPresenterTest {
    @Mock
    private ScoresLoader scoresLoader;
    @Mock
    private LoaderManager loaderManager;
    @Mock
    private ScoresContract.View view;

    private ScoresPresenter scoresPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        scoresPresenter = new ScoresPresenter(view, scoresLoader, loaderManager);
    }

    @Test
    public void testScoresPresenter_loaderManagerShouldInitLoader() throws Exception {
        scoresPresenter.start();

        verify(loaderManager).initLoader(
                eq(ScoresPresenter.LOADER_ID),
                eq(null),
                any(LoaderManager.LoaderCallbacks.class));
    }
}