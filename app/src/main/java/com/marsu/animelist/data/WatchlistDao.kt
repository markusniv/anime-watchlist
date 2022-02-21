package com.marsu.animelist.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.marsu.animelist.model.Entry

@Dao
interface WatchlistDao {

    // SQL queries for Entry-model

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEntry(entry : Entry)

    @Query("SELECT * FROM watchlist_table")
    fun getAllEntries(): LiveData<List<Entry>>
}