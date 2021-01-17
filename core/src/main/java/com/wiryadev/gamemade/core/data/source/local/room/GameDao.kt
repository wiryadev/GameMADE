package com.wiryadev.gamemade.core.data.source.local.room

import androidx.room.*
import com.wiryadev.gamemade.core.data.source.local.entity.GameCacheEntity
import com.wiryadev.gamemade.core.data.source.local.entity.GameEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface GameDao {
    @Query("SELECT * FROM tblGameCaches")
    fun getAllCachedGame(): Flow<List<GameCacheEntity>>

    @Query("SELECT * FROM tblGameLibraries")
    fun getGameLibraries(): Flow<List<GameEntity>>

    @Query("SELECT EXISTS (SELECT 1 FROM tblGameLibraries WHERE id=:id)")
    fun checkFavorite(id: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameCaches(gameCaches: List<GameCacheEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameToLibrary(game: GameEntity)

    @Delete
    suspend fun deleteGameFromLibrary(game: GameEntity)
}