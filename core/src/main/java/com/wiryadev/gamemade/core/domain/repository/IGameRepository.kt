package com.wiryadev.gamemade.core.domain.repository

import com.wiryadev.gamemade.core.data.Resource
import com.wiryadev.gamemade.core.domain.model.Game
import kotlinx.coroutines.flow.Flow

interface IGameRepository {

    fun getGameList(): Flow<Resource<List<Game>>>

    suspend fun searchGame(search: String): Resource<List<Game>>

    fun getGameLibraries(): Flow<List<Game>>

    fun getDetailGame(id: Int): Flow<Resource<Game>>

    fun checkFavorite(id: Int): Flow<Int>

    suspend fun insertGameToLibrary(game: Game)

    suspend fun deleteGameFromLibrary(game: Game)

}