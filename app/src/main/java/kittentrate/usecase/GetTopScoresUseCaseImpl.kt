package kittentrate.usecase

import io.reactivex.Observable
import kittentrate.db.PlayerScoreDao
import kittentrate.ui.score.PlayerScore

/**
 * Created by Manuel Lorenzo on 11/02/2018.
 */
class GetTopScoresUseCaseImpl(private val playerScoreDao: PlayerScoreDao) : GetTopScoresUseCase {
    override fun getTopScores(): Observable<List<PlayerScore>> {
        return playerScoreDao.getTopScores()
    }
}


