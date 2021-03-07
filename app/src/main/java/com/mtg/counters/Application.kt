package com.mtg.counters

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.mtg.counters.db.AppDatabase

class Application: MultiDexApplication() {

    init {
        instance = this
    }

    companion object {
        val TAG = "Counters Application"
        private var instance: Application? = null
        private var database: AppDatabase? = null

        fun getContext(): Context {
            return instance!!.applicationContext
        }

        fun getDatabase(): AppDatabase {
            return database!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(getContext())
        database = AppDatabase.getInMemoryDatabase(getContext())
    }
}