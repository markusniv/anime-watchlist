package com.marsu.animelist.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "watchlist_table")
data class Entry(
    val mal_id: Int,
    val title: String,
    val image_src: String,
    val status: String,
    val episodes: Int,
    val episodes_watched: Int,
) : Parcelable
