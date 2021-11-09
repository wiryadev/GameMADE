package com.wiryadev.gamemade.core.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wiryadev.gamemade.core.data.GameRepository.Companion.NETWORK_PAGE_SIZE
import com.wiryadev.gamemade.core.data.source.remote.RemoteDataSource
import com.wiryadev.gamemade.core.domain.model.Game
import com.wiryadev.gamemade.core.utils.DataMapper
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class GamePagingSource(
    private val remoteDataSource: RemoteDataSource
) : PagingSource<Int, Game>() {
    override fun getRefreshKey(state: PagingState<Int, Game>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = remoteDataSource.getGameList(position, params.loadSize)
            val games = DataMapper.mapResponseToDomain(response.results)

            val prevKey = if (position == STARTING_PAGE_INDEX) null else (position - 1)
            val nextKey = if (games.isEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }

            LoadResult.Page(
                data = games,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}