package kittentrate.repository.datasource

import io.reactivex.Flowable
import kittentrate.ui.score.PlayerScore

/**
 * Created by Manuel Lorenzo on 12/02/2018.
 */
interface DatabaseDataSource {
    fun getTopScores(): Flowable<List<PlayerScore>>

    fun addTopScore(playerScore: PlayerScore)
}