package com.wiryadev.gamemade.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GameResponse(
    @field:SerializedName("id")
    var id: Int,

    @field:SerializedName("name")
    var title: String,

    @field:SerializedName("released")
    var released: String? = null,

    @field:SerializedName("metacritic")
    var metacritic: Int? = null,

    @field:SerializedName("metacritic_url")
    var metacriticUrl: Int? = null,

    @field:SerializedName("background_image")
    var bgImage: String? = null,

    @field:SerializedName("description")
    var description: String? = null,

    @field:SerializedName("game_series_count")
    var gameSeriesCount: Int? = 0
)