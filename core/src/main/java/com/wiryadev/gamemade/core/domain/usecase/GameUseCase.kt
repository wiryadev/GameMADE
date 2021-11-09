package com.wiryadev.gamemade.core.domain.usecase

import androidx.paging.PagingData
import com.wiryadev.gamemade.core.data.Resource
import com.wiryadev.gamemade.core.domain.model.Game
import kotlinx.coroutines.flow.Flow

interface GameUseCase {

    fun getGameList(): Flow<PagingData<Game>>

    fun getSearchResults(query: String): Flow<PagingData<Game>>

    suspend fun searchGame(search: String): List<Game>

    fun getGameLibraries(): Flow<PagingData<Game>>

    suspend fun getDetailGame(id: Int): Flow<Resource<Game>>

    fun checkFavorite(id: Int): Flow<Int>

    suspend fun insertGameToLibrary(game: Game)

    suspend fun deleteGameFromLibrary(game: Game)

}