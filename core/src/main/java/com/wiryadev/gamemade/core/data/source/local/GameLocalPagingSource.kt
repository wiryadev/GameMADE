package com.wiryadev.gamemade.core.data.source.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wiryadev.gamemade.core.data.GameRepository
import com.wiryadev.gamemade.core.data.GameRepository.Companion.STARTING_PAGE_INDEX
import com.wiryadev.gamemade.core.data.source.remote.RemoteDataSource
import com.wiryadev.gamemade.core.domain.model.Game
import com.wiryadev.gamemade.core.utils.DataMapper
import retrofit2.HttpException
import java.io.IOException

class GameLocalPagingSource(
    private val localDataSource: LocalDataSource,
    private val query: String? = null
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
            val data = DataMapper.mapEntitiesToDomain(localDataSource.getGameLibraries())

            val prevKey = if (position == STARTING_PAGE_INDEX) null else (position - 1)
            val nextKey = if (data.isEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / GameRepository.NETWORK_PAGE_SIZE)
            }

            LoadResult.Page(
                data = data,
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