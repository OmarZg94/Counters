package com.mtg.counters.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mtg.counters.Application
import com.mtg.counters.db.AppDatabase
import com.mtg.counters.db.dao.CountersDao
import com.mtg.counters.db.entities.Counters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var database: AppDatabase
    private lateinit var countersDao: CountersDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        countersDao = database.countersDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() = database.close()

    @Test
    @Throws(Exception::class)
    fun insertCounter() {
        val counter = Counters("asdfaky7","Azucar", 0L)
        countersDao.insert(counter)
        countersDao.selectAll().observeOnce {
            Log.i(Application.TAG, "Counters Size = ${it.size}")
        }
    }

    private fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
        val observer = LiveDataObserver(handler = onChangeHandler)
        GlobalScope.launch(Dispatchers.Main) { observe(observer, observer) }
    }
}