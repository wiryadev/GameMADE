package com.wiryadev.gamemade.core.data.source.remote

import android.util.Log
import com.wiryadev.gamemade.core.data.source.remote.network.ApiResponse
import com.wiryadev.gamemade.core.data.source.remote.network.ApiService
import com.wiryadev.gamemade.core.data.source.remote.response.GameResponse
import com.wiryadev.gamemade.core.data.source.remote.response.ListGameResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getGameList(page: Int, pageSize: Int): ListGameResponse =
        apiService.getGameList(page, pageSize)

    suspend fun getSearchResults(page: Int, pageSize: Int, query: String): ListGameResponse =
        apiService.getGameList(page, pageSize, query)

    suspend fun searchGame(search: String): Flow<List<GameResponse>> =
        flow<List<GameResponse>> {
            apiService.searchGame(search).results
        }.flowOn(Dispatchers.IO)

    suspend fun getDetailGame(id: Int): Flow<ApiResponse<GameResponse>> =
        flow {
            try {
                val response = apiService.getDetailGame(id)
                if (response != null) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty(EMPTY))
                }
            } catch (ex: Exception) {
                emit(ApiResponse.Error(ex.message.toString()))
                Log.e(TAG, "getDetailGame: ${ex.message} ")
            }
        }.flowOn(Dispatchers.IO)

    companion object {
        private const val TAG = "RemoteDataSource"
        const val EMPTY = "EmptyResult"
    }

}