package kittentrate.scores

import kittentrate.RobolectricTestHelper
import kittentrate.repository.Repository
import kittentrate.ui.score.ScoresContract
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

/**
 * Created by Manuel Lorenzo
 */
@RunWith(RobolectricTestRunner::class)
class ScoresPresenterTest : RobolectricTestHelper() {
    @Mock
    private lateinit var view: ScoresContract.View
    @Mock
    private lateinit var repository: Repository

    private lateinit var scoresPresenter: ScoresPresenter

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        scoresPresenter = ScoresPresenter(view, repository)
    }

    @Test
    fun `ScoresPresenter should initialize itself by fetching the top scores from the repo`() {
        scoresPresenter.start()

        verify(repository).topScores
    }
}