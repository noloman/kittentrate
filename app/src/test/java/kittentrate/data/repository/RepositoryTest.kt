package kittentrate.data.repository


import com.nhaarman.mockito_kotlin.whenever
import kittentrate.GameApplication
import kittentrate.RobolectricTestHelper
import kittentrate.api.NetworkDataSourceImpl
import kittentrate.db.Database
import kittentrate.repository.Repository
import kittentrate.ui.score.PlayerScore
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner

/**
 * Created by Manuel Lorenzo on 18/11/2017.
 */
@RunWith(RobolectricTestRunner::class)
class RepositoryTest : RobolectricTestHelper() {
    private lateinit var repository: Repository
    @Mock
    private lateinit var gameLocalDataSource: GameLocalDataSource
    @Mock
    private lateinit var networkDataSourceImpl: NetworkDataSourceImpl
    @Mock
    private lateinit var sharedPreferencesDataSource: GameDataSource.SharedPreferencesDataSource
    @Mock
    private lateinit var database: Database
    @Mock
    private lateinit var application: GameApplication.Companion

    @Before
    fun setUp() {
        repository = Repository.getInstance(
                gameLocalDataSource,
                networkDataSourceImpl,
                sharedPreferencesDataSource)
        whenever(GameApplication.database).thenReturn(database)
    }

    @After
    fun tearDown() {
        Repository.destroy()
    }

    @Test
    fun `trying to get top scores from repository should query the local data source`() {
        repository.topScores

        verify(gameLocalDataSource).topScores
    }

    @Test
    fun `adding top score through repository should query the local data source`() {
        val playerScore = PlayerScore("Pepe", 1)
        repository.addTopScore(playerScore)

        verify(gameLocalDataSource).addTopScore(playerScore)
    }

    @Test
    fun `setting photo tag should use the SharedPreferencesDataSource`() {
        repository.preferencesPhotoTag = "bla"

        verify(sharedPreferencesDataSource).preferencesPhotoTag = eq("bla")
    }

    @Test
    fun `getting the preferences photo tag should use the SharedPreferencesDataSource`() {
        whenever(sharedPreferencesDataSource.preferencesPhotoTag).thenReturn("bla")
        repository.preferencesPhotoTag

        verify(sharedPreferencesDataSource).preferencesPhotoTag
    }
}