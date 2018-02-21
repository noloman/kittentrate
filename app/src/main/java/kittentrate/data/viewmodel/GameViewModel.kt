package kittentrate.data.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.widget.ViewFlipper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kittentrate.data.mapping.PhotoEntityMapper
import kittentrate.data.model.FlickrPhoto
import kittentrate.data.model.PhotoEntity
import kittentrate.repository.Repository
import kittentrate.ui.game.Card
import kittentrate.ui.game.Game
import kittentrate.ui.score.PlayerScore
import kittentrate.utils.Constants
import java.util.concurrent.TimeUnit
import javax.inject.Inject

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
class GameViewModel @Inject constructor(val game: Game, val repository: Repository) : ViewModel() {
    var photosMutableLiveData: MutableLiveData<List<PhotoEntity>> = MutableLiveData()
    val networkViewStateMutableLiveData: MutableLiveData<NetworkViewState> = MutableLiveData()

    private val facingUpCardsHashMap = HashMap<Int, Card>(Constants.NUMBER_MATCHING_CARDS)
    val viewFlipperCardHashMap = HashMap<ViewFlipper, Card>(Constants.NUMBER_MATCHING_CARDS)

    var gameMutableLiveData: MutableLiveData<Game> = MutableLiveData()

    init {
        gameMutableLiveData.value = game
    }

    /**
     * Method that handles all the logic of the game. It is given a {@position} and a {@card} and
     * checks first, whether {@facingUpCardsHashMap} already contains a card in the {@position}
     * or not.
     *
     * If it does NOT, it assigns the given {@card} in the {@facingUpCardsHashMap} to the position.
     * Then it checks whether it should check for matching cards, and if it does need to, it
     * uses the id of the {@card} and iterates through {@facingUpCardsHashMap} and if the id is
     * already there, the match hasn't been found.
     *
     * If the match has been found, it iterates
     * through {@facingUpCardsHashMap} and removes the card from {@photosMutableLiveData}. Then
     * the {@facingUpCardsHashMap} is cleared up and increases the game score.
     *
     * If the match was never found, it just decreases the game score and turns the already facing
     * up cards to be facing down, so the turn can start again
     */
    fun cardFlipped(position: Int, card: Card) {
        if (!facingUpCardsHashMap.containsKey(position)) {
            facingUpCardsHashMap[position] = card
            if (shouldCheckForCardsMatch()) {
                // This would be the last card, we should check for matches.
                val id = card.id
                var matchFound = true
                for ((_, loopCard) in facingUpCardsHashMap) {
                    if (loopCard.id != id) {
                        matchFound = false
                    }
                }
                if (matchFound) {
                    var itemShouldBeRemoved = false
                    val itemToBeRemoved = mutableListOf<PhotoEntity>()
                    // Match found; remove MATCHED cards from data set
                    for ((_, facingUpCard) in facingUpCardsHashMap.asIterable()) {
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
                    facingUpCardsHashMap.clear()
                    increaseGameScore()
                } else {
                    // Match not found; turn cards over and start from the beginning.
                    decreaseGameScore()
                    turnCardsOver()
                }
            }
        }
    }

    /**
     * Resets the score of the game to be 0 again
     */
    private fun resetScore() {
        val game: Game? = gameMutableLiveData.value
        if (game != null) {
            game.score = 0
            gameMutableLiveData.value = game
        }
    }

    /**
     * Method that checks whether two cards match, by comparing the size of the
     * {@facingUpCardsHashMap} and the {@Constants.NUMBER_MATCHING_CARDS}
     */
    private fun shouldCheckForCardsMatch(): Boolean {
        return facingUpCardsHashMap.size == Constants.NUMBER_MATCHING_CARDS
    }

    /**
     * Increases the score of the game by 1
     */
    private fun increaseGameScore() {
        val game: Game? = gameMutableLiveData.value
        if (game != null) {
            game.score = game.score + 1
            gameMutableLiveData.value = game
        }
    }

    /**
     * Decreases the score of the game by 1
     */
    private fun decreaseGameScore() {
        val game: Game? = gameMutableLiveData.value
        if (game != null) {
            game.score = game.score - 1
            gameMutableLiveData.value = game
        }
    }

    /**
     * Method called on starting the ViewModel
     * that fetches the available photos from the Repository
     */
    fun getPhotos() {
        val mapper = PhotoEntityMapper()
        repository
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

    /**
     * Method called whenever the kittens menu item is clicked
     */
    fun onKittensMenuItemClicked() {
        repository.setPreferencesPhotoTag(Constants.PHOTO_TAG_KITTEN_VALUE)
        repository.getPhotosWithSavedTag()
        resetScore()
    }

    /**
     * Method called whenever the puppies menu item is clicked
     */
    fun onPuppiesMenuItemClicked() {
        repository.setPreferencesPhotoTag(Constants.PHOTO_TAG_PUPPY_VALUE)
        repository.getPhotosWithSavedTag()
        resetScore()
    }

    /**
     * Method that checks whether the user should be allowed to tap on the screen.
     * This happens only when the number of cards that are facing up is different than
     * {@NUMBER_MATCHING_CARDS}
     */
    fun shouldDispatchTouchEvent(): Boolean {
        return facingUpCardsHashMap.size != Constants.NUMBER_MATCHING_CARDS
    }

    /**
     * Method called whenever the player enters a new score after completing the game
     */
    fun onScoredEntered(playerScore: PlayerScore) {
        repository.addTopScore(playerScore)
    }

    /**
     * Rotates the cards back again to be facing down
     */
    private fun turnCardsOver() {
        Completable.complete()
                .delay(Constants.ROTATION_TIME.toLong(),
                        TimeUnit.MILLISECONDS,
                        AndroidSchedulers.mainThread())
                .doOnComplete {
                    for (viewFlipper in viewFlipperCardHashMap.keys) {
                        viewFlipper.showPrevious()
                    }
                    viewFlipperCardHashMap.clear()
                    facingUpCardsHashMap.clear()
                    photosMutableLiveData.postValue(photosMutableLiveData.value)
                }
                .subscribe()
    }
}