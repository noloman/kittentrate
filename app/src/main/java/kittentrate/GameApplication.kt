package kittentrate

import android.app.Application
import android.content.Context
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import kittentrate.di.*

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
class GameApplication : Application() {
    private lateinit var refWatcher: RefWatcher

    companion object {
        lateinit var application: ApplicationComponent
        fun getRefWatcher(context: Context): RefWatcher {
            val gameApplication: GameApplication = context.applicationContext as GameApplication
            return gameApplication.refWatcher
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        refWatcher = LeakCanary.install(this)
        // Normal app init code...}
        application = DaggerApplicationComponent
                .builder()
                .appModule(AppModule(this))
                .databaseModule(DatabaseModule(this))
                .repositoryModule(RepositoryModule())
                .build()
        application.inject(this)
    }
}
