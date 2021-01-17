package com.wiryadev.gamemade.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class SearchGameResponse(
    @field:SerializedName("count")
    var count: Int,

    @field:SerializedName("next")
    var next: Int,

    @field:SerializedName("previous")
    var previous: String?,

    @field:SerializedName("results")
    var results: List<GameResponse>,

    @field:SerializedName("user_platforms")
    var userPlatforms: Boolean
)
