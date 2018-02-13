package kittentrate.data.domain

import kittentrate.RobolectricTestHelper
import kittentrate.ui.game.Card
import kittentrate.ui.game.GameManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

/**
 * Created by Manuel Lorenzo
 */
@RunWith(RobolectricTestRunner::class)
class GameManagerTest : RobolectricTestHelper() {
    @Mock
    private val gamePresenter: GamePresenter? = null
    @Mock
    private val card1: Card? = null
    @Mock
    private val card2: Card? = null

    private var gameManager: GameManager? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        gameManager = GameManager(gamePresenter!!)
    }

    @Test
    @Throws(Exception::class)
    fun testGameManager_gamePresenterShouldTurnCardsOver_whenCardsAreFlipped() {
        `when`(card1!!.id).thenReturn("1")
        `when`(card2!!.id).thenReturn("2")
        gameManager!!.cardFlipped(0, card1)
        gameManager!!.cardFlipped(1, card2)

        verify<GamePresenter>(gamePresenter).onTurnCardsOver()
    }

    @Test
    @Throws(Exception::class)
    fun testGameManager_gamePresenterShouldRemoveViewFlipperAndChangeScore_whenCardsAreMatched() {
        `when`(card1!!.id).thenReturn("1")

        gameManager!!.cardFlipped(0, card1)
        gameManager!!.cardFlipped(1, card1)
        verify<GamePresenter>(gamePresenter, times(2)).removeViewFlipper()
        verify<GamePresenter>(gamePresenter, times(2)).notifyAdapterItemRemoved(anyString())
        verify<GamePresenter>(gamePresenter).onGameScoreChanged(anyInt())
    }

    @Test
    @Throws(Exception::class)
    fun testGameManager_gamePresenterShouldNotifyItemsChanged_whenGameManagerFlipsCard() {
        `when`(card1!!.id).thenReturn("1")
        gameManager!!.cardFlipped(0, card1)
        gameManager!!.removeCardsFromMap()

        verify<GamePresenter>(gamePresenter).notifyAdapterItemChanged(anyInt())
    }
}