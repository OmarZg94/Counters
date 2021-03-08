package com.mtg.counters.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mtg.counters.db.entities.Counters

@Dao
interface CountersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(counter: Counters)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(counters: MutableList<Counters>)

    @Query("SELECT * FROM counters ORDER BY title ASC")
    fun selectAll(): LiveData<MutableList<Counters>>

    @Query("DELETE FROM counters")
    suspend fun deleteAll()
}