package kittentrate.game

import kittentrate.utils.Constants
import java.util.*

/**
 * Created by Manuel Lorenzo on 21/03/2017.
 */

class GameManager(private val gamePresenter: GameContract.Presenter) : GameDomainContract.Manager {
    private val facingUpCardsWeakHashMap = WeakHashMap<Int, Card>(Constants.NUMBER_MATCHING_CARDS)
    private val game: Game = Game()

    private val gameScore: Int
        get() = game.score

    override fun cardFlipped(position: Int, card: Card) {
        if (!facingUpCardsWeakHashMap.containsKey(position)) {
            facingUpCardsWeakHashMap.put(position, card)
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
                    // Match found; remove cards from dataset
                    gamePresenter.removeViewFlipper()
                    for ((_, card1) in facingUpCardsWeakHashMap) {
                        gamePresenter.notifyAdapterItemRemoved(card1.id)
                    }
                    facingUpCardsWeakHashMap.clear()
                    increaseGameScore()
                    gamePresenter.removeViewFlipper()
                } else {
                    // Match not found; turn cards over and start from the beginning.
                    decreaseGameScore()
                    gamePresenter.onTurnCardsOver()
                }
                gamePresenter.onGameScoreChanged(gameScore)
            }
        }
    }

    override fun removeCardsFromMap() {
        for ((key) in facingUpCardsWeakHashMap) {
            gamePresenter.notifyAdapterItemChanged(key)
        }
        facingUpCardsWeakHashMap.clear()
    }

    override fun shouldDispatchUiEvent(): Boolean {
        return facingUpCardsWeakHashMap.size != Constants.NUMBER_MATCHING_CARDS
    }

    override fun resetScore() {
        resetGameScore()
    }

    private fun shouldCheckForCardsMatch(): Boolean {
        return facingUpCardsWeakHashMap.size == Constants.NUMBER_MATCHING_CARDS
    }

    private fun increaseGameScore() {
        game.score = game.score + 1
    }

    private fun decreaseGameScore() {
        game.score = game.score - 1
    }

    private fun resetGameScore() {
        game.score = 0
    }
}
