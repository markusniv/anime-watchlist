package com.marsu.animelist.viewmodel

import androidx.lifecycle.ViewModel
import com.marsu.animelist.context.App
import com.marsu.animelist.data.WatchlistDatabase
import com.marsu.animelist.repository.EntryRepository

class HomeViewModel : ViewModel() {

    private val entryRepository: EntryRepository

    init {
        val entryDao = WatchlistDatabase.getDatabase(App.appContext).watchlistDao()
        entryRepository = EntryRepository(entryDao)
    }

    fun getAllEntries() = entryRepository.getAllEntries()
}