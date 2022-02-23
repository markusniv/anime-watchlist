package com.marsu.animelist.viewmodel

import android.app.AlertDialog
import android.widget.Toast
import androidx.lifecycle.*
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
    val alertObservable: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
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
            var page = 1
            var hasNextPage = true
            var alerted = false

            while (hasNextPage && page < 5) {
                val result = if (App.sfw) EntryApi.retrofitService.getEntriesSfw(query, page, App.sfw);
                else EntryApi.retrofitService.getEntries(query, page);
                val json : JsonObject = result.asJsonObject
                val data = json.get("data")
                val pagination = json.get("pagination").asJsonObject
                hasNextPage = pagination.get("has_next_page").asBoolean
                if (!alerted) {
                    val totalPages = pagination.get("last_visible_page").asInt
                    if (totalPages > 5) {
                        alertObservable.postValue(true)
                        alerted = true
                    }
                }
                val arr = data.asJsonArray
                for (entry in arr) {
                    val single = entry.asJsonObject
                    val images = single.getAsJsonObject("images").getAsJsonObject("jpg")
                    val aired = single.getAsJsonObject("aired")
                    val episodes : Int? = try {
                        single.get("episodes").asInt
                    } catch (e : Exception) {
                        null
                    }
                    val year : Int? = try {
                        single.get("year").asInt
                    } catch (e : Exception) {
                        null
                    }
                    val season : String? = try {
                        single.get("season").asString.replaceFirstChar { it.uppercase() }
                    } catch (e : Exception) {
                        null
                    }
                    val airedFrom : String? = try {
                        aired.get("from").asString.split("T")[0]
                    } catch (e : Exception) {
                        null
                    }
                    val airedTo : String? = try {
                        aired.get("to").asString.split("T")[0]
                    } catch (e : Exception) {
                        null
                    }
                    val score : Int? = try {
                        single.get("score").asInt
                    } catch (e : Exception) {
                        null
                    }
                    val newEntry = Entry(
                        single.get("mal_id").asInt,
                        single.get("type").asString,
                        single.get("title").asString,
                        images.get("image_url").asString,
                        year,
                        season,
                        airedFrom,
                        airedTo,
                        score,
                        0
                    )
                    entryList.add(newEntry)

                }
                page++
                observableEntries.postValue(entryList)
                Thread.sleep(1000)
            }
            alertObservable.postValue(false)
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