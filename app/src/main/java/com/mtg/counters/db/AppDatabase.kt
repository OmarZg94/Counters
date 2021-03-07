package com.mtg.counters.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mtg.counters.db.dao.CountersDao
import com.mtg.counters.db.entities.Counters

private const val DB_NAME = "counters"
private const val DB_VERSION = 1

@Database(entities = [
    Counters::class
], version = DB_VERSION, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun countersDao(): CountersDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getInMemoryDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
