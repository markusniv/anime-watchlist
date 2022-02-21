package com.marsu.animelist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.marsu.animelist.model.Entry

/**     (c) Markus Nivasalo 2022
 *
 *      RoomDB class for the watchlist database
 */

@Database(entities = [Entry::class], version = 1, exportSchema = false)
abstract class WatchlistDatabase : RoomDatabase() {

    abstract fun watchlistDao(): WatchlistDao

    companion object {
        @Volatile
        private var INSTANCE: WatchlistDatabase? = null

        fun getDatabase(context: Context): WatchlistDatabase {

            // If database exists, return it
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            // Else create a new one and return it
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WatchlistDatabase::class.java,
                    "watchlist_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}