package com.wiryadev.gamemade.core.data

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
    override fun searchGame(search: String): Flow<Resource<List<Game>>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = remoteDataSource.searchGame(search).first()) {
                is ApiResponse.Success -> {
                    val result = DataMapper.mapResponseToDomain(apiResponse.data) as Flow<List<Game>>
                    emitAll(result.map { Resource.Success(it) })
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error<List<Game>>(apiResponse.toString()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error<List<Game>>(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun getGameLibraries(): Flow<List<Game>> {
        return localDataSource.getGameLibraries().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun getDetailGame(id: Int): Flow<Resource<Game>> {
        return flow {
            remoteDataSource.getDetailGame(id)
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