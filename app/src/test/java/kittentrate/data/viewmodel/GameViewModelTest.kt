package kittentrate.data.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import kittentrate.repository.Repository
import kittentrate.ui.game.Card
import kittentrate.ui.game.Game
import org.junit.*
import org.junit.Assert.assertEquals

/**
 * Copyright 2018 Manuel Lorenzo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class GameViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repository = mock<Repository>()
    private val game = mock<Game>()
    private val card1 = mock<Card>()
    private val card2 = mock<Card>()

    private lateinit var gameViewModel: GameViewModel

    companion object {
        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
        }

        @AfterClass
        @JvmStatic
        fun afterClass() {
            RxAndroidPlugins.reset()
        }
    }

    @Before
    fun setUp() {
        gameViewModel = GameViewModel(game, repository)
    }

    @After
    fun tearDown() {
    }

    @Test
    @Throws(Exception::class)
    fun `GameViewModel should call turnCardsOver when cards are flipped`() {
        whenever(card1.id).thenReturn("1")
        whenever(card2.id).thenReturn("2")

        gameViewModel.cardFlipped(0, card1)
        assertEquals(gameViewModel.facingUpCardsHashMap.size, 1)

        gameViewModel.cardFlipped(1, card2)
        assertEquals(gameViewModel.facingUpCardsHashMap.size, 0)
    }

    @Test
    fun `GameViewModel should remove view flipper and change the score when cards are matched`() {
        whenever(card1.id).thenReturn("1")

        gameViewModel.cardFlipped(0, card1)
        assertEquals(gameViewModel.facingUpCardsHashMap.size, 1)

        gameViewModel.cardFlipped(1, card1)
        assertEquals(gameViewModel.facingUpCardsHashMap.size, 0)
        verify(game).increaseScore()
    }

    @Test
    @Throws(Exception::class)
    fun `GameViewModel should change put the card into the facingUpCardsHashMap`() {
        whenever(card1.id).thenReturn("1")

        assertEquals(0, gameViewModel.facingUpCardsHashMap.size)
        gameViewModel.cardFlipped(0, card1)
        assertEquals(1, gameViewModel.facingUpCardsHashMap.size)
    }
}