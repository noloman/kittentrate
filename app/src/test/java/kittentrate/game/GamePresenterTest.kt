package kittentrate.game

import com.nhaarman.mockito_kotlin.isA
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import kittentrate.RobolectricTestHelper
import kittentrate.data.repository.GameRepository
import kittentrate.data.repository.model.PhotoEntity
import kittentrate.data.viewmodel.NetworkViewState
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

/**
 * Created by Manuel Lorenzo
 */
@RunWith(RobolectricTestRunner::class)
class GamePresenterTest : RobolectricTestHelper() {
    @Mock
    private lateinit var view: GameContract.View
    @Mock
    private lateinit var gameRepository: GameRepository

    private lateinit var gamePresenter: GameContract.Presenter

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        gamePresenter = GamePresenter(gameRepository, view)
    }

    @Test
    fun `view should start as None and after starting it should have a Loading state`() {
        //verify<GameContract.View>(view).networkViewState = isA<NetworkViewState.None>()
        whenever(gameRepository.photos).thenReturn(Observable.just(listOf()))
        gamePresenter.start()
        verify<GameContract.View>(view).networkViewState = isA<NetworkViewState.Loading>()
        verify<GameContract.View>(view).networkViewState = isA<NetworkViewState.Success<List<PhotoEntity>>>()
    }

    @Test
    fun `view should start as None and after starting it should have an Error state`() {
        //verify<GameContract.View>(view).networkViewState = isA<NetworkViewState.None>()
        whenever(gameRepository.photos).thenReturn(Observable.error(Throwable()))
        gamePresenter.start()
        verify<GameContract.View>(view).networkViewState = isA<NetworkViewState.Loading>()
        verify<GameContract.View>(view).networkViewState = isA<NetworkViewState.Error>()
    }

    @Test
    fun `should remove view flipper when removing cards from maps`() {
        gamePresenter.removeCardsFromMaps()
        verify<GameContract.View>(view).removeViewFlipper()
    }

    @Test
    fun `should check for game end after score changes`() {
        gamePresenter.onGameScoreChanged(1)

        verify<GameContract.View>(view).onScoreChanged(eq(1))
        verify<GameContract.View>(view).checkGameFinished()
    }
}