package kittentrate

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import kittentrate.api.ApiService
import kittentrate.api.RetrofitClient
import kittentrate.data.preferences.SharedPreferencesDataSourceImpl
import kittentrate.db.Database
import kittentrate.repository.Repository

/**
 * Created by Manuel Lorenzo
 */

class GameApplication : Application() {
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
