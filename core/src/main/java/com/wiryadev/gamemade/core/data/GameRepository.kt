package com.wiryadev.gamemade.core.data

import android.util.Log
import com.wiryadev.gamemade.core.data.source.local.LocalDataSource
import com.wiryadev.gamemade.core.data.source.remote.RemoteDataSource
import com.wiryadev.gamemade.core.data.source.remote.network.ApiResponse
import com.wiryadev.gamemade.core.data.source.remote.response.GameResponse
import com.wiryadev.gamemade.core.domain.model.Game
import com.wiryadev.gamemade.core.domain.repository.IGameRepository
import com.wiryadev.gamemade.core.utils.DataMapper
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : IGameRepository {

    override fun getGameList(): Flow<Resource<List<Game>>> {
        return object : NetworkBoundResource<List<Game>, List<GameResponse>>() {
            override fun loadFromDB(): Flow<List<Game>> {
                return localDataSource.getAllCachedGame().map {
                    DataMapper.mapCacheEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Game>?): Boolean =
                data.isNullOrEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<GameResponse>>> =
                remoteDataSource.getGameList()

            override suspend fun saveCallResult(data: List<GameResponse>) {
                localDataSource.insertGameCaches(
                    DataMapper.mapResponseToCacheEntities(data)
                )
            }
        }.asFlow()
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun searchGame(search: String): Resource<List<Game>> {
        return when (val apiResponse = remoteDataSource.searchGame(search).first()) {
            is ApiResponse.Success -> {
                val result = DataMapper.mapResponseToDomain(apiResponse.data)
                Resource.Success(result)
            }
            is ApiResponse.Empty -> {
                Resource.Error(apiResponse.toString())
            }
            is ApiResponse.Error -> {
                Resource.Error(apiResponse.errorMessage)
            }
        }
    }

    override fun getGameLibraries(): Flow<List<Game>> {
        return localDataSource.getGameLibraries().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override suspend fun getDetailGame(id: Int): Flow<Resource<Game>> {
        Log.d("Repo", "getDetailGame: called")
        return remoteDataSource.getDetailGame(id).map {
            when (it) {
                is ApiResponse.Success -> Resource.Success(DataMapper.mapDetailResponseToDomain(it.data))
                is ApiResponse.Empty -> Resource.Error(it.toString())
                is ApiResponse.Error -> Resource.Error(it.errorMessage)
            }
        }
    }

    override fun checkFavorite(id: Int): Flow<Int> {
        return flow {
            localDataSource.checkFavorite(id)
        }
    }

    override suspend fun insertGameToLibrary(game: Game) {
        return localDataSource.insertGameToLibrary(
            DataMapper.mapDomainToEntity(game)
        )
    }

    override suspend fun deleteGameFromLibrary(game: Game) {
        return localDataSource.deleteGameFromLibrary(
            DataMapper.mapDomainToEntity(game)
        )
    }
}