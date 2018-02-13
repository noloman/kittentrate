package kittentrate.usecase

import io.reactivex.Observable
import kittentrate.ui.score.PlayerScore

/**
 * Created by Manuel Lorenzo on 12/02/2018.
 */
interface GetTopScoresUseCase {
    fun getTopScores(): Observable<List<PlayerScore>>
}