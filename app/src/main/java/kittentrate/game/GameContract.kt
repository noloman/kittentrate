package kittentrate.game

import android.widget.ViewFlipper

import kittentrate.data.repository.model.PhotoEntity
import kittentrate.score.PlayerScore

/**
 * Created by Manuel Lorenzo on 21/03/2017.
 */

interface GameContract {
    interface View {
        fun showLoadingView()

        fun hideLoadingView()

        fun turnCardsOver()

        fun notifyAdapterItemChanged(pos: Int)

        fun notifyAdapterItemRemoved(id: String)

        fun setAdapterData(photoEntityList: List<PhotoEntity>)

        fun removeViewFlipper()

        fun showErrorView()

        fun shouldDispatchTouchEvent(): Boolean

        fun onScoreChanged(gameScore: Int)

        fun checkGameFinished()
    }

    interface Presenter {
        fun onItemClicked(position: Int, card: Card, viewFlipper: ViewFlipper)

        fun removeCardsFromMaps()

        fun start()

        fun onScoredEntered(playerScore: PlayerScore)

        fun notifyAdapterItemChanged(key: Int?)

        fun onTurnCardsOver()

        fun removeViewFlipper()

        fun onGameScoreChanged(gameScore: Int)

        fun notifyAdapterItemRemoved(id: String)

        fun onKittensMenuItemClicked()

        fun onPuppiesMenuItemClicked()
    }
}
