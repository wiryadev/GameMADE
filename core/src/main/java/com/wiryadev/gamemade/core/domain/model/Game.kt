package com.wiryadev.gamemade.core.domain.model

data class Game(
    var id: Int,
    var title: String,
    var released: String? = null,
    var metacritic: Int? = null,
    var metacriticUrl: String? = null,
    var bgImage: String? = null,
    var description: String? = null,
    var gameSeriesCount: Int? = 0
)
