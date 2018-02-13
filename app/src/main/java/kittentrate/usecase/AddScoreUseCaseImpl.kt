package kittentrate.usecase

import io.reactivex.Observable
import kittentrate.db.PlayerScoreDao
import kittentrate.ui.score.PlayerScore

/**
 * Created by Manuel Lorenzo on 11/02/2018.
 */
class AddScoreUseCaseImpl(private val playerScoreDao: PlayerScoreDao) : AddScoreUseCase {
    override fun addTopScore(playerScore: PlayerScore): Observable<Long> {
        return playerScoreDao.addTopScore(playerScore)
    }
}
