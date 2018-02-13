package kittentrate.data.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kittentrate.db.PlayerScoreDao

/**
 * Created by Manuel Lorenzo on 11/02/2018.
 */
class ScoresViewModel(private val playerScoreDao: PlayerScoreDao) : ViewModel() {
    private lateinit var responseViewStateMutableLiveData: MutableLiveData<ResponseViewState>

    fun getTopScores() {
        playerScoreDao.getTopScores()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { scores ->
                            responseViewStateMutableLiveData.value =
                                    ResponseViewState.Success(scores)
                        })
    }
}