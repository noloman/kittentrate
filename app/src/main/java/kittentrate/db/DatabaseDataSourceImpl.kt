package kittentrate.db

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kittentrate.repository.datasource.DatabaseDataSource
import kittentrate.ui.score.PlayerScore

/**
 * Created by Manuel Lorenzo on 12/02/2018.
 */
class DatabaseDataSourceImpl(val playerScoreDao: PlayerScoreDao) : DatabaseDataSource {
    override fun getTopScores(): Flowable<List<PlayerScore>> {
        return playerScoreDao.getTopScores()
    }

    override fun addTopScore(playerScore: PlayerScore): Single<Long> {
        return Single.just(playerScoreDao.addTopScore(playerScore))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}