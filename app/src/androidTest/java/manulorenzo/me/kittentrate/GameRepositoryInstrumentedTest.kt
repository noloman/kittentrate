package manulorenzo.me.kittentrate

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import kittentrate.data.repository.local.KittentrateScoresDatabase
import kittentrate.score.PlayerScore
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Manuel Lorenzo on 25/11/2017.
 */
@RunWith(AndroidJUnit4::class)
class GameRepositoryInstrumentedTest {
    private lateinit var database: KittentrateScoresDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                KittentrateScoresDatabase::class.java)
                .build()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `trying to get top scores from repository should query the local data source`() {
        val a = database.playerScoresDao().getTopScores()

        assert(a.isNotEmpty())
    }

    @Test
    fun `adding top score through repository should query the local data source`() {
        val playerScore = PlayerScore("Pepe", 1)
        database.playerScoresDao().addTopScore(playerScore)

        assert(database.playerScoresDao().getTopScores().isNotEmpty())
    }
}