package kittentrate.di

import kittentrate.GameApplication
import kittentrate.repository.Repository

/**
 * Created by Manuel Lorenzo on 04/04/2017.
 */

object Injection {
    fun provideRepository(): Repository {
        return GameApplication.repository
    }
}