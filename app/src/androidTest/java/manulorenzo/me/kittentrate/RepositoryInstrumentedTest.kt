package manulorenzo.me.kittentrate

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import kittentrate.db.Database
import kittentrate.ui.score.PlayerScore
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Manuel Lorenzo on 25/11/2017.
 */
@RunWith(AndroidJUnit4::class)
class RepositoryInstrumentedTest {
    private lateinit var database: Database

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                Database::class.java)
                .build()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `trying to get top scores from repository should query the local data source`() {
        val a = database.playerScoreDao().getTopScores()

        assert(a.isNotEmpty())
    }

    @Test
    fun `adding top score through repository should query the local data source`() {
        val playerScore = PlayerScore("Pepe", 1)
        database.playerScoreDao().addTopScore(playerScore)

        assert(database.playerScoreDao().getTopScores().isNotEmpty())
    }
}