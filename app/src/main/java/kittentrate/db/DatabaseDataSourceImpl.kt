package kittentrate.db

import io.reactivex.Observable
import kittentrate.repository.datasource.DatabaseDataSource
import kittentrate.ui.score.PlayerScore

/**
 * Created by Manuel Lorenzo on 12/02/2018.
 */
class DatabaseDataSourceImpl(val playerScoreDao: PlayerScoreDao) : DatabaseDataSource {
    override fun getTopScores(): Observable<List<PlayerScore>> {
        return playerScoreDao.getTopScores()
    }

    override fun addTopScore(playerScore: PlayerScore): Observable<Long> {
        return playerScoreDao.addTopScore(playerScore)
    }
}