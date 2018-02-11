package kittentrate

import android.app.Application
import android.arch.persistence.room.Room

import com.squareup.leakcanary.LeakCanary
import kittentrate.data.repository.local.KittentrateScoresDatabase

/**
 * Created by Manuel Lorenzo
 */

class GameApplication : Application() {
    companion object {
        lateinit var database: KittentrateScoresDatabase
    }

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
        // Normal app init code...}
        database = Room.databaseBuilder(this,
                KittentrateScoresDatabase::class.java,
                "KittentrateScoresDatabase")
                .build()
    }
}
