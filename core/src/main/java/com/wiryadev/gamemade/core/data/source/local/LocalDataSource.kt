package com.wiryadev.gamemade.core.data.source.local

import com.wiryadev.gamemade.core.data.source.local.entity.GameCacheEntity
import com.wiryadev.gamemade.core.data.source.local.entity.GameEntity
import com.wiryadev.gamemade.core.data.source.local.room.GameDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val dao: GameDao) {

    fun getAllCachedGame(): Flow<List<GameCacheEntity>> = dao.getAllCachedGame()

    fun getGameLibraries(): Flow<List<GameEntity>> = dao.getGameLibraries()

    fun checkFavorite(id: Int): Flow<Int> = dao.checkFavorite(id)

    suspend fun insertGameCaches(gameCaches: List<GameCacheEntity>) = dao.insertGameCaches(gameCaches)

    suspend fun insertGameToLibrary(game: GameEntity) = dao.insertGameToLibrary(game)

    suspend fun deleteGameFromLibrary(game: GameEntity) = dao.deleteGameFromLibrary(game)

}