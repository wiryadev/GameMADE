package com.wiryadev.gamemade.core.data.source.remote.network

import com.wiryadev.gamemade.core.data.source.remote.response.GameResponse
import com.wiryadev.gamemade.core.data.source.remote.response.ListGameResponse
import com.wiryadev.gamemade.core.data.source.remote.response.SearchGameResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("games")
    suspend fun getGameList(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
    ): ListGameResponse

    @GET("games")
    suspend fun searchGame(
        @Query("search") search: String,
        @Query("search_precise") searchPrecise: Boolean = true
    ): SearchGameResponse

    @GET("games/{id}")
    suspend fun getDetailGame(
        @Path("id") id: Int,
    ): GameResponse?

}