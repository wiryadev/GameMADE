package com.wiryadev.gamemade.core.data.source.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wiryadev.gamemade.core.domain.model.Game
import com.wiryadev.gamemade.core.utils.DataMapper
import java.io.IOException

private const val STARTING_PAGE_INDEX = 0

class GameLocalPagingSource(
    private val localDataSource: LocalDataSource,
    private val query: String? = null
) : PagingSource<Int, Game>() {

    override fun getRefreshKey(state: PagingState<Int, Game>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val data = DataMapper.mapEntitiesToDomain(localDataSource.getGameLibraries(params.loadSize))

            val prevKey = if (position == STARTING_PAGE_INDEX) null else (position - 1)
            val nextKey = if (data.isNullOrEmpty()) null else position + 1

            LoadResult.Page(
                data = data,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        }
    }
}