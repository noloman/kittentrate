package kittentrate.data.viewmodel.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import kittentrate.data.viewmodel.GameViewModel
import kittentrate.data.viewmodel.ScoresViewModel
import kittentrate.repository.Repository
import kittentrate.ui.game.Game

class GameViewModelFactory(val game: Game, val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoresViewModel::class.java)) {
            return GameViewModel(game, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}