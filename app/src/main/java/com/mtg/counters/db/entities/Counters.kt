package com.mtg.counters.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "counters")
data class Counters(@PrimaryKey(autoGenerate = false) val id: String,
                    @ColumnInfo(name = "title") val title: String,
                    @ColumnInfo(name = "count") val count: Long)
