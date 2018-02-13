package kittentrate.data.local;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import kittentrate.ui.score.PlayerScore;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

/**
 * Created by Manuel Lorenzo on 10/05/2017.
 */
@RunWith(AndroidJUnit4.class)
public class GameLocalDataSourceTest {
    private GameLocalDataSource localDataSource;

    @Before
    public void setup() {
        GameLocalDataSource.destroyInstance();
        localDataSource = GameLocalDataSource.getInstance(InstrumentationRegistry.getTargetContext());
        localDataSource.deleteAllScores();
    }

    @Test
    public void testLocalDataSource_shouldSavePlayerScore() {
        PlayerScore playerScore = new PlayerScore("Manu", 1);
        localDataSource.addTopScore(playerScore);

        assertThat(localDataSource.getTopScores(), hasItem(playerScore));
    }

    @Test
    public void testLocalDataSource_shouldSaveTwoAndOnlyTwoPlayerScore() {
        PlayerScore playerScore1 = new PlayerScore("Manu", 1);
        PlayerScore playerScore2 = new PlayerScore("Manu", 2);
        localDataSource.addTopScore(playerScore1);
        localDataSource.addTopScore(playerScore2);

        List<PlayerScore> topScores = localDataSource.getTopScores();
        assertThat(topScores, hasItems(playerScore1, playerScore2));
        assertThat(topScores.size(), is(2));
    }
}