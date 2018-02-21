package kittentrate.data.viewmodel.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import kittentrate.data.viewmodel.GameViewModel
import kittentrate.repository.Repository
import kittentrate.ui.game.Game
import javax.inject.Inject

class GameViewModelFactory @Inject constructor(val game: Game, val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GameViewModel(game, repository) as T
    }
}