package kittentrate

import android.app.Application
import android.arch.persistence.room.Room

import com.squareup.leakcanary.LeakCanary
import kittentrate.db.Database

/**
 * Created by Manuel Lorenzo
 */

class GameApplication : Application() {
    companion object {
        lateinit var database: Database
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
                Database::class.java,
                "Database")
                .build()
    }
}
