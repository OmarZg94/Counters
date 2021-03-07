package com.mtg.counters.db.managers

import com.mtg.counters.Application
import com.mtg.counters.db.entities.Counters
import kotlinx.coroutines.runBlocking

class CountersMgr {
    companion object {
        fun insertCounters(counters: MutableList<Counters>) = runBlocking {
            Application.getDatabase().countersDao().deleteAll()
            Application.getDatabase().countersDao().insertAll(counters)
        }
    }
}