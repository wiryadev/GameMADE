package com.wiryadev.gamemade.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblGameCaches")
data class GameCacheEntity(
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

    @ColumnInfo(name = "bg_image")
    var bgImage: String? = null
)
