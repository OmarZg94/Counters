package com.mtg.counters

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class Application: MultiDexApplication() {

    init {
        instance = this
    }

    companion object {
        val TAG = "Counters Application"
        private var instance: Application? = null

        fun getContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(getContext())
    }
}