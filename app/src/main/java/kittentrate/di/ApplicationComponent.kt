package kittentrate.di

import dagger.Component
import kittentrate.GameApplication
import kittentrate.data.viewmodel.GameViewModel
import kittentrate.data.viewmodel.factory.GameViewModelFactory
import kittentrate.ui.game.GameFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RepositoryModule::class, DatabaseModule::class])
interface ApplicationComponent {
    fun inject(gameViewModel: GameViewModel)
    fun inect(gameViewModelFactory: GameViewModelFactory)
    fun inject(gameApplication: GameApplication)
    fun inject(gameFragment: GameFragment)
}