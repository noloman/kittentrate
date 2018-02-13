package kittentrate.data.viewmodel.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import kittentrate.data.viewmodel.ScoresViewModel
import kittentrate.db.PlayerScoreDao

/**
 * Created by Manuel Lorenzo on 11/02/2018.
 */
class LocalViewModelFactory(private val playerScoreDao: PlayerScoreDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoresViewModel::class.java)) {
            return ScoresViewModel(playerScoreDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}