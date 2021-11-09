package com.wiryadev.gamemade.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wiryadev.gamemade.core.data.source.GamePagingSource
import com.wiryadev.gamemade.core.data.source.local.LocalDataSource
import com.wiryadev.gamemade.core.data.source.remote.RemoteDataSource
import com.wiryadev.gamemade.core.data.source.remote.network.ApiResponse
import com.wiryadev.gamemade.core.domain.model.Game
import com.wiryadev.gamemade.core.domain.repository.IGameRepository
import com.wiryadev.gamemade.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : IGameRepository {

    override fun getGameList(): Flow<PagingData<Game>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                GamePagingSource(remoteDataSource)
            }
        ).flow
    }

    override suspend fun searchGame(search: String): Resource<List<Game>> {
        return when (val apiResponse = remoteDataSource.searchGame(search).first()) {
            is ApiResponse.Success -> {
                val result = DataMapper.mapResponseToDomain(apiResponse.data)
                Resource.Success(result)
            }
            is ApiResponse.Empty -> {
                Resource.Error(apiResponse.message)
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
        return remoteDataSource.getDetailGame(id).map { response ->
            when (response) {
                is ApiResponse.Success -> Resource.Success(DataMapper.mapDetailResponseToDomain(response.data))
                is ApiResponse.Empty -> Resource.Error(response.message)
                is ApiResponse.Error -> Resource.Error(response.errorMessage)
            }
        }
    }

    override fun checkFavorite(id: Int): Flow<Int> {
        return localDataSource.checkFavorite(id)
    }

    override suspend fun insertGameToLibrary(game: Game) {
        localDataSource.insertGameToLibrary(
            DataMapper.mapDomainToEntity(game)
        )
    }

    override suspend fun deleteGameFromLibrary(game: Game) {
        localDataSource.deleteGameFromLibrary(
            DataMapper.mapDomainToEntity(game)
        )
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}