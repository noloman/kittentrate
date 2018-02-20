package kittentrate.data.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import kittentrate.repository.Repository
import kittentrate.ui.game.Game
import org.junit.*


class GameViewModelTest {
    @Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    val repository = mock<Repository>()
    val game = mock<Game>()

    lateinit var gameViewModel: GameViewModel

    @BeforeClass
    fun beforeClas() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
    }

    @Before
    fun setUp() {
        GameViewModel(game, repository)
    }

    @After
    fun tearDown() {
    }

    @AfterClass
    fun afterClass() {
        RxAndroidPlugins.reset()
    }
}