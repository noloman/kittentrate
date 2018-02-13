package kittentrate.ui.game

/**
 * Created by Manuel Lorenzo
 */

interface GameDomainContract {
    interface Manager {
        fun cardFlipped(position: Int, card: Card)

        fun removeCardsFromMap()

        fun shouldDispatchUiEvent(): Boolean

        fun resetScore()
    }
}
