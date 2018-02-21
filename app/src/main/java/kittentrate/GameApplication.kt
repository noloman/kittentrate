package kittentrate

import android.app.Activity
import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import android.support.v4.app.Fragment
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import kittentrate.api.ApiService
import kittentrate.api.RetrofitClient
import kittentrate.data.preferences.SharedPreferencesDataSourceImpl
import kittentrate.db.Database
import kittentrate.di.DaggerApplicationComponent
import kittentrate.repository.Repository
import javax.inject.Inject

/**
 * Copyright 2018 Manuel Lorenzo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class GameApplication : Application(), HasActivityInjector, HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidFragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingAndroidFragmentInjector
    }

    private lateinit var refWatcher: RefWatcher

    companion object {
        lateinit var database: Database
        lateinit var flickrApi: ApiService
        lateinit var repository: Repository
        fun getRefWatcher(context: Context): RefWatcher {
            val gameApplication: GameApplication = context.applicationContext as GameApplication
            return gameApplication.refWatcher
        }
    }

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.create().inject(this)
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        refWatcher = LeakCanary.install(this)
        // Normal app init code...}
        database = Room.databaseBuilder(this,
                Database::class.java,
                "Database")
                .build()
        flickrApi = RetrofitClient.getRetrofitClient()
        val sharedPreferencesDataSource = SharedPreferencesDataSourceImpl(this)
        repository = Repository(flickrApi, database.playerScoreDao(), sharedPreferencesDataSource)
    }
}
