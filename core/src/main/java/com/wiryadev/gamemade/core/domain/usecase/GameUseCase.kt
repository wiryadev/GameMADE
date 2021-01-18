package com.wiryadev.gamemade.core.domain.usecase

import com.wiryadev.gamemade.core.data.Resource
import com.wiryadev.gamemade.core.data.source.remote.network.ApiResponse
import com.wiryadev.gamemade.core.domain.model.Game
import kotlinx.coroutines.flow.Flow

interface GameUseCase {

    fun getGameList(): Flow<Resource<List<Game>>>

    suspend fun searchGame(search: String): Resource<List<Game>>

    fun getGameLibraries(): Flow<List<Game>>

    suspend fun getDetailGame(id: Int): Flow<Resource<Game>>

    fun checkFavorite(id: Int): Flow<Int>

    suspend fun insertGameToLibrary(game: Game)

    suspend fun deleteGameFromLibrary(game: Game)

}