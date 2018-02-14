package kittentrate.repository

import io.reactivex.Flowable
import io.reactivex.Observable
import kittentrate.api.ApiService
import kittentrate.data.model.FlickrPhoto
import kittentrate.db.PlayerScoreDao
import kittentrate.repository.datasource.DatabaseDataSource
import kittentrate.repository.datasource.NetworkDataSource
import kittentrate.repository.datasource.SharedPreferencesDataSource
import kittentrate.ui.score.PlayerScore
import kittentrate.utils.applySchedulers

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
class Repository(private val flickrApi: ApiService,
                 private val playerScoreDao: PlayerScoreDao,
                 private val sharedPreferencesDataSource: SharedPreferencesDataSource) :
        NetworkDataSource, DatabaseDataSource, SharedPreferencesDataSource {

    override fun getTopScores(): Flowable<List<PlayerScore>> {
        return playerScoreDao.getTopScores()
    }

    override fun addTopScore(playerScore: PlayerScore) {
        playerScoreDao.addTopScore(playerScore)
    }

    override fun setPreferencesPhotoTag(photoTag: String) {
        sharedPreferencesDataSource.setPreferencesPhotoTag(photoTag)
    }

    override fun getPreferencesPhotoTag(): String = sharedPreferencesDataSource.getPreferencesPhotoTag()

    override fun getPhotosWithSavedTag(): Observable<FlickrPhoto> {
        return flickrApi
                .getPhotos(getPreferencesPhotoTag())
                .applySchedulers()
    }
}
