package kittentrate.data.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import android.widget.ViewFlipper
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import kittentrate.data.mapping.PhotoEntityMapper
import kittentrate.data.model.FlickrPhoto
import kittentrate.data.model.PhotoEntity
import kittentrate.di.Injection
import kittentrate.ui.game.Card
import kittentrate.ui.game.Game
import kittentrate.ui.score.PlayerScore
import kittentrate.utils.Constants
import java.util.concurrent.ConcurrentHashMap

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
class GameViewModel : ViewModel() {
    var photosMutableLiveData: MutableLiveData<List<PhotoEntity>> = MutableLiveData()
    val networkViewStateMutableLiveData: MutableLiveData<NetworkViewState> = MutableLiveData()

    private val facingUpCardsConcurrentHashMap = ConcurrentHashMap<Int, Card>(Constants.NUMBER_MATCHING_CARDS)
    val viewFlipperCardWeakHashMap = ConcurrentHashMap<ViewFlipper, Card>(Constants.NUMBER_MATCHING_CARDS)

    var gameMutableLiveData: MutableLiveData<Game> = MutableLiveData()

    init {
        gameMutableLiveData.value = Game()
    }

    fun cardFlipped(position: Int, card: Card) {
        if (!facingUpCardsConcurrentHashMap.containsKey(position)) {
            facingUpCardsConcurrentHashMap[position] = card
            if (shouldCheckForCardsMatch()) {
                // This would be the last card, we should check for matches.
                val id = card.id
                var matchFound = true
                for ((_, loopCard) in facingUpCardsConcurrentHashMap) {
                    if (loopCard.id != id) {
                        matchFound = false
                    }
                }
                if (matchFound) {
                    var itemShouldBeRemoved = false
                    val itemToBeRemoved = mutableListOf<PhotoEntity>()
                    // Match found; remove MATCHED cards from data set
                    for ((_, facingUpCard) in facingUpCardsConcurrentHashMap.asIterable()) {
                        val newPhotos = photosMutableLiveData.value?.toMutableList()
                        val it = newPhotos?.iterator()
                        if (it != null) {
                            while (it.hasNext()) {
                                val next = it.next()
                                if (facingUpCard.id == next.id) {
                                    itemShouldBeRemoved = true
                                    itemToBeRemoved.add(next)
                                }
                            }
                        }
                    }
                    if (itemShouldBeRemoved) {
                        photosMutableLiveData.value?.toMutableList().let {
                            it?.removeAll(itemToBeRemoved)
                            photosMutableLiveData.postValue(it?.toList())
                        }
                    }
                    facingUpCardsConcurrentHashMap.clear()
                    increaseGameScore()
                } else {
                    // Match not found; turn cards over and start from the beginning.
                    decreaseGameScore()
                    turnCardsOver()
                }
            }
        }
    }

    private fun resetScore() {
        val game: Game? = gameMutableLiveData.value
        if (game != null) {
            game.score = 0
            gameMutableLiveData.value = game
        }
    }

    private fun shouldCheckForCardsMatch(): Boolean {
        return facingUpCardsConcurrentHashMap.size == Constants.NUMBER_MATCHING_CARDS
    }

    private fun increaseGameScore() {
        val game: Game? = gameMutableLiveData.value
        if (game != null) {
            game.score = game.score + 1
            gameMutableLiveData.value = game
        }
    }

    private fun decreaseGameScore() {
        val game: Game? = gameMutableLiveData.value
        if (game != null) {
            game.score = game.score - 1
            gameMutableLiveData.value = game
        }
    }

    fun getPhotos() {
        val mapper = PhotoEntityMapper()
        Injection.provideRepository()
                .getPhotosWithSavedTag()
                .flatMap { t: FlickrPhoto ->
                    Observable.fromCallable { mapper.mapToEntity(t.photos.photo) }
                }
                .doOnSubscribe {
                    gameMutableLiveData.value = Game()
                    networkViewStateMutableLiveData.postValue(NetworkViewState.Loading())
                }
                .subscribeBy(
                        onNext = { photoEntityList: List<PhotoEntity>? ->
                            photosMutableLiveData.postValue(photoEntityList)
                            networkViewStateMutableLiveData.postValue(NetworkViewState.Success(photoEntityList))
                        },
                        onError = {
                            networkViewStateMutableLiveData.postValue(NetworkViewState.Error(it.message))
                        })
    }

    // Presenter methods
    fun onKittensMenuItemClicked() {
        val repository = Injection.provideRepository()
        repository.setPreferencesPhotoTag(Constants.PHOTO_TAG_KITTEN_VALUE)
        repository.getPhotosWithSavedTag()
        resetScore()
    }

    fun onPuppiesMenuItemClicked() {
        val repository = Injection.provideRepository()
        repository.setPreferencesPhotoTag(Constants.PHOTO_TAG_PUPPY_VALUE)
        repository.getPhotosWithSavedTag()
        resetScore()
    }

    fun shouldDispatchTouchEvent(): Boolean {
        return facingUpCardsConcurrentHashMap.size != Constants.NUMBER_MATCHING_CARDS
    }

    fun onScoredEntered(playerScore: PlayerScore) {
        val repository = Injection.provideRepository()
        repository.addTopScore(playerScore)
    }

    private fun turnCardsOver() {
        Handler().postDelayed({
            facingUpCardsConcurrentHashMap.clear()
            // TODO no idea
            // removeViewFlipper()
            photosMutableLiveData.postValue(photosMutableLiveData.value)
        }, Constants.ROTATION_TIME.toLong())
    }
}