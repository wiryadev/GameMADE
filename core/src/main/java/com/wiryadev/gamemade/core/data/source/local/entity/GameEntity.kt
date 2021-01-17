package com.wiryadev.gamemade.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblGameLibraries")
data class GameEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "name")
    var title: String,

    @ColumnInfo(name = "released")
    var released: String? = null,

    @ColumnInfo(name = "metacritic")
    var metacritic: Int? = null,

    @ColumnInfo(name = "metacritic_url")
    var metacriticUrl: Int? = null,

    @ColumnInfo(name = "bg_image")
    var bgImage: String? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "game_series_count")
    var gameSeriesCount: Int? = 0
)