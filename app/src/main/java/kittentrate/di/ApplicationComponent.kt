package kittentrate.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import kittentrate.GameApplication
import kittentrate.data.viewmodel.GameViewModel

@Component(modules = [AndroidInjectionModule::class, AppModule::class])
interface ApplicationComponent {
    fun inject(gameViewModel: GameViewModel)
    fun inject(gameApplication: GameApplication)
}