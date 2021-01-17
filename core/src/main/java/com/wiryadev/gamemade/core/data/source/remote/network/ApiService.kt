package com.wiryadev.gamemade.core.data.source.remote.network

import com.wiryadev.gamemade.core.BuildConfig
import com.wiryadev.gamemade.core.data.source.remote.response.GameResponse
import com.wiryadev.gamemade.core.data.source.remote.response.ListGameResponse
import com.wiryadev.gamemade.core.data.source.remote.response.SearchGameResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("games")
    suspend fun getGameList(
        @Query("key") key: String = BuildConfig.API_KEY
    ): ListGameResponse

    @GET("games")
    suspend fun searchGame(
        @Query("search") search: String,
        @Query("key") key: String = BuildConfig.API_KEY,
        @Query("search_precise") searchPrecise: Boolean = true
    ): SearchGameResponse

    @GET("games/{id}")
    suspend fun getDetailGame(
        @Path("id") id: Int,
        @Query("key") key: String = BuildConfig.API_KEY,
    ): GameResponse?

}