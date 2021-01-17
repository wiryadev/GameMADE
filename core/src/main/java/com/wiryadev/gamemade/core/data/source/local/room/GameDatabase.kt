package com.wiryadev.gamemade.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wiryadev.gamemade.core.data.source.local.entity.GameCacheEntity
import com.wiryadev.gamemade.core.data.source.local.entity.GameEntity

@Database(
    entities = [GameEntity::class, GameCacheEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GameDatabase : RoomDatabase() {

    abstract fun GameDao(): GameDao

}