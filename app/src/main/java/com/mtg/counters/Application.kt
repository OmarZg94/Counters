package com.mtg.counters

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.mtg.counters.db.AppDatabase
import com.mtg.counters.utils.Preferences

@SuppressLint("StaticFieldLeak")
class Application : MultiDexApplication() {

    init {
        instance = this
    }

    companion object {
        val TAG = "Counters Application"
        private var instance: Application? = null
        private var database: AppDatabase? = null
        private var prefs: Preferences? = null

        fun getContext(): Context {
            return instance!!.applicationContext
        }

        fun getDatabase(): AppDatabase {
            return database!!
        }

        fun getPreferences(): Preferences {
            return prefs!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(getContext())
        prefs = Preferences(getContext())
        database = AppDatabase.getInMemoryDatabase(getContext())
    }
}