package com.marsu.animelist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.marsu.animelist.App
import com.marsu.animelist.data.WatchlistDatabase
import com.marsu.animelist.model.Entry
import com.marsu.animelist.network.EntryApi
import com.marsu.animelist.repository.EntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class SearchViewModel : ViewModel() {

    private val entryRepository: EntryRepository
    private var entryList: MutableList<Entry>
    val observableEntries: MutableLiveData<List<Entry>> by lazy {
        MutableLiveData<List<Entry>>()
    }

    init {
        val entryDao = WatchlistDatabase.getDatabase(App.appContext).watchlistDao()
        entryRepository = EntryRepository(entryDao)
        entryList = mutableListOf()
    }

    fun addEntry(entry: Entry) = entryRepository.addEntry(entry)

    fun getSearchResults(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            entryList = mutableListOf()
            val result = EntryApi.retrofitService.getEntries(query, true);
            val json : JsonObject = result.asJsonObject
            val data = json.get("data")
            val arr = data.asJsonArray
            for (entry in arr) {
                val single = entry.asJsonObject
                val images = single.getAsJsonObject("images").getAsJsonObject("jpg")
                val episodes : Int? = try {
                    single.get("episodes").asInt
                } catch (e : Exception) {
                    null
                }
                val newEntry = Entry(
                    single.get("mal_id").asInt,
                    single.get("title").asString,
                    images.get("image_url").asString,
                    single.get("airing").asBoolean,
                    episodes
                )
                entryList.add(newEntry)
            }
            observableEntries.postValue(entryList)
        }
    }
}

class SearchViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}