package com.wiryadev.gamemade.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListGameResponse(
    @field:SerializedName("count")
    var count: Int,

    @field:SerializedName("next")
    var next: String?,

    @field:SerializedName("previous")
    var previous: String?,

    @field:SerializedName("results")
    var results: List<GameResponse>
)
