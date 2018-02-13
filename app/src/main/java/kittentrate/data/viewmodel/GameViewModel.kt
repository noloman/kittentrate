package kittentrate.data.viewmodel

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kittentrate.GameApplication
import kittentrate.data.mapping.PhotoEntityMapper
import kittentrate.data.model.FlickrPhoto
import kittentrate.data.model.PhotoEntity
import kittentrate.di.Injection
import kittentrate.ui.game.Card
import kittentrate.ui.game.Game
import kittentrate.ui.score.PlayerScore
import kittentrate.utils.Constants
import java.util.*

/**
 * Created by Manuel Lorenzo on 11/02/2018.
 */
class GameViewModel : AndroidViewModel(GameApplication()) {
    val networkViewStateMutableLiveData: MutableLiveData<NetworkViewState> = MutableLiveData()
    //var score: Observable<Int> by Delegates.observable(gameScore) { _: KProperty<*>, _: Observable<Int>, _: Observable<Int> -> }
    private val facingUpCardsWeakHashMap = WeakHashMap<Int, Card>(Constants.NUMBER_MATCHING_CARDS)
    private val gameObservable: Observable<Game> = Observable.just(Game())

    val gameScore: Observable<Int>
        get() = gameObservable.map { t: Game -> t.score }

    fun cardFlipped(position: Int, card: Card) {
        if (!facingUpCardsWeakHashMap.containsKey(position)) {
            facingUpCardsWeakHashMap[position] = card
            if (shouldCheckForCardsMatch()) {
                // This would be the last card, we should check for matches.
                val id = card.id
                var matchFound = true
                for ((_, loopCard) in facingUpCardsWeakHashMap) {
                    if (loopCard.id != id) {
                        matchFound = false
                    }
                }
                if (matchFound) {
                    // Match found; remove MATCHED cards from data set
                    removeViewFlipper()
                    for ((_, card1) in facingUpCardsWeakHashMap) {
                        notifyAdapterItemRemoved(card1.id)
                    }
                    facingUpCardsWeakHashMap.clear()
                    increaseGameScore()
                    removeViewFlipper()
                } else {
                    // Match not found; turn cards over and start from the beginning.
                    decreaseGameScore()
                    onTurnCardsOver()
                }
                //gameViewModel.onGameScoreChanged(gameScore)
            }
        }
    }

    fun removeCardsFromMap() {
        for ((key) in facingUpCardsWeakHashMap) {
            notifyAdapterItemChanged(key)
        }
        facingUpCardsWeakHashMap.clear()
    }

    fun shouldDispatchUiEvent(): Boolean {
        return facingUpCardsWeakHashMap.size != Constants.NUMBER_MATCHING_CARDS
    }

    fun resetScore() {
        resetGameScore()
    }

    private fun shouldCheckForCardsMatch(): Boolean {
        return facingUpCardsWeakHashMap.size == Constants.NUMBER_MATCHING_CARDS
    }

    private fun increaseGameScore() {
        gameObservable.map { t: Game -> t.score += 1 }
    }

    private fun decreaseGameScore() {
        gameObservable.map { t: Game -> t.score -= 1 }
    }

    private fun resetGameScore() {
        gameObservable.map { t: Game -> t.score = 0 }
    }

    fun getPhotos() {
        val getPhotosUseCase = Injection.provideGetPhotosUseCase(GameApplication().applicationContext)
        val mapper = PhotoEntityMapper()
        getPhotosUseCase
                .getPhotosWithSavedTag()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { t: FlickrPhoto -> Observable.just(mapper.mapToEntity(t.photos.photo)) }
                .subscribeBy(
                        onNext = { list: List<PhotoEntity>? ->
                            networkViewStateMutableLiveData.value = NetworkViewState.Success(list)
                        },
                        onError = {
                            networkViewStateMutableLiveData.value = NetworkViewState.Error(it.message)
                        })
    }

    // Presenter methods
    fun onKittensMenuItemClicked() {
        val repository = Injection.provideRepository(GameApplication().applicationContext)
        repository.setPreferencesPhotoTag(Constants.PHOTO_TAG_KITTEN_VALUE)
        repository.getPhotosWithSavedTag()
        resetScore()
    }

    fun onPuppiesMenuItemClicked() {
        val repository = Injection.provideRepository(GameApplication().applicationContext)
        repository.setPreferencesPhotoTag(Constants.PHOTO_TAG_PUPPY_VALUE)
        repository.getPhotosWithSavedTag()
        resetScore()
    }

    fun removeCardsFromMaps() {
        removeCardsFromMap()
    }

    fun shouldDispatchTouchEvent(): Boolean {
        return shouldDispatchUiEvent()
    }

    fun onScoredEntered(playerScore: PlayerScore): Observable<Long> {
        return Injection.provideAddScoreUseCase(GameApplication().applicationContext)
                .addTopScore(playerScore)
    }

    fun removeViewFlipper() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun notifyAdapterItemRemoved(id: String) {

    }

    fun onTurnCardsOver() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //fun onGameScoreChanged(gameScore: Int) {
    //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    //}

    fun notifyAdapterItemChanged(key: Int?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}