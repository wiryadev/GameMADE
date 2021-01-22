package com.wiryadev.gamemade.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListGameResponse(
    @field:SerializedName("results")
    var results: List<GameResponse>
)
