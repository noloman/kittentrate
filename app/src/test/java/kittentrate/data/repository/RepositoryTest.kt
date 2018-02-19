package kittentrate.data.repository


import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import kittentrate.RobolectricTestHelper
import kittentrate.api.ApiService
import kittentrate.db.PlayerScoreDao
import kittentrate.repository.Repository
import kittentrate.repository.datasource.SharedPreferencesDataSource
import kittentrate.ui.score.PlayerScore
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify

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

class RepositoryTest : RobolectricTestHelper() {
    private lateinit var repository: Repository
    private val flickrApiMock = mock<ApiService>()
    private val playerScoreDaoMock = mock<PlayerScoreDao>()
    private val sharedPreferencesDataSource = mock<SharedPreferencesDataSource>()

    @Before
    fun setUp() {
        repository = Repository(flickrApiMock, playerScoreDaoMock, sharedPreferencesDataSource)
    }

    @Test
    fun `when getting top scores from repository it should retrieve Pepe and 100 points from the local data source`() {
        val listPlayerScore: List<PlayerScore> = mutableListOf(PlayerScore("Pepe", 100))
        whenever(playerScoreDaoMock.getTopScores()).thenReturn(Flowable.just(listPlayerScore))

        TestSubscriber<List<PlayerScore>>().apply {

            repository.getTopScores().subscribe(this)

            assertNoErrors()
            assertValue(listPlayerScore)
        }
    }

    @Test
    fun `trying to get top scores from repository should query the local data source`() {
        repository.getTopScores()

        verify(playerScoreDaoMock).getTopScores()
    }

    @Test
    fun `adding top score through repository should query the local data source`() {
        val playerScore = PlayerScore("Pepe", 1)
        repository.addTopScore(playerScore)

        verify(playerScoreDaoMock).addTopScore(playerScore)
    }

    @Test
    fun `setting photo tag should use the SharedPreferencesDataSource`() {
        whenever(sharedPreferencesDataSource.getPreferencesPhotoTag()).thenReturn(Observable.just("bla"))

        repository.setPreferencesPhotoTag("bla")

        TestObserver<String>().apply {
            sharedPreferencesDataSource.getPreferencesPhotoTag().subscribe(this)
            assertNoErrors()
            assertValue("bla")
        }
    }

    @Test
    fun `getting the preferences photo tag should use the SharedPreferencesDataSource`() {
        whenever(sharedPreferencesDataSource.getPreferencesPhotoTag()).thenReturn(Observable.just("bla"))

        TestObserver<String>().apply {
            repository.getPreferencesPhotoTag().subscribe(this)
            awaitTerminalEvent()
            assertNoErrors().assertValue("bla")
        }
    }
}