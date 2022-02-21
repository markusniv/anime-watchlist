package com.marsu.animelist.repository

import com.marsu.animelist.data.WatchlistDao
import com.marsu.animelist.model.Entry

class EntryRepository(private val watchlistDao: WatchlistDao) {

    fun addEntry(entry: Entry) = watchlistDao.addEntry(entry)
    fun getAllEntries() = watchlistDao.getAllEntries()
}