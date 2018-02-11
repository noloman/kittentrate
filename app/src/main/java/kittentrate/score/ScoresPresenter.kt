package kittentrate.score

import kittentrate.data.repository.GameRepository

/**
 * Created by Manuel Lorenzo
 */

class ScoresPresenter(private val view: ScoresContract.View,
                      private val repository: GameRepository) : ScoresContract.Presenter {

    override fun start() {
        repository.topScores
                .subscribe({ playerScores -> view.showScores(playerScores) })
    }
}
