package com.wiryadev.gamemade.core.data.source.remote

import android.util.Log
import com.wiryadev.gamemade.core.data.source.remote.network.ApiResponse
import com.wiryadev.gamemade.core.data.source.remote.network.ApiService
import com.wiryadev.gamemade.core.data.source.remote.response.GameResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getGameList(): Flow<ApiResponse<List<GameResponse>>> =
        flow {
            try {
                val response = apiService.getGameList()
                val data = response.results
                if (data.isNotEmpty()) {
                    emit(ApiResponse.Success(data))
                } else {
                    emit(ApiResponse.Empty(EMPTY))
                }
            } catch (ex: Exception) {
                emit(ApiResponse.Error(ex.message.toString()))
                Log.e(TAG, "getGameList: ${ex.message} ")
            }
        }.flowOn(Dispatchers.IO)

    suspend fun searchGame(search: String): Flow<ApiResponse<List<GameResponse>>> =
        flow {
            try {
                val response = apiService.searchGame(search)
                val data = response.results
                if (response.count > 0) {
                    emit(ApiResponse.Success(data))
                } else {
                    emit(ApiResponse.Empty(EMPTY))
                }
            } catch (ex: Exception) {
                emit(ApiResponse.Error(ex.message.toString()))
                Log.e(TAG, "getGameList: ${ex.message} ")
            }
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