package com.wiryadev.gamemade.core.utils

import com.wiryadev.gamemade.core.data.source.local.entity.GameCacheEntity
import com.wiryadev.gamemade.core.data.source.local.entity.GameEntity
import com.wiryadev.gamemade.core.data.source.remote.response.GameResponse
import com.wiryadev.gamemade.core.domain.model.Game

object DataMapper {

    fun mapResponseToCacheEntities(input: List<GameResponse>): List<GameCacheEntity> {
        return input.map {
            GameCacheEntity(
                id = it.id,
                title = it.title,
                released = it.released,
                metacritic = it.metacritic,
                bgImage = it.bgImage
            )
        }
    }

    fun mapResponseToDomain(input: List<GameResponse>): List<Game> {
        return input.map {
            Game(
                id = it.id,
                title = it.title,
                released = it.released,
                metacritic = it.metacritic,
                bgImage = it.bgImage
            )
        }
    }

    fun mapCacheEntitiesToDomain(input: List<GameCacheEntity>): List<Game> {
        return input.map {
            Game(
                id = it.id,
                title = it.title,
                released = it.released,
                metacritic = it.metacritic,
                bgImage = it.bgImage
            )
        }
    }

    fun mapEntitiesToDomain(input: List<GameEntity>): List<Game> {
        return input.map {
            Game(
                id = it.id,
                title = it.title,
                released = it.released,
                metacritic = it.metacritic,
                metacriticUrl = it.metacriticUrl,
                bgImage = it.bgImage,
                gameSeriesCount = it.gameSeriesCount
            )
        }
    }

    fun mapDomainToEntity(input: Game): GameEntity {
        return GameEntity(
            id = input.id,
            title = input.title,
            released = input.released,
            metacritic = input.metacritic,
            metacriticUrl = input.metacriticUrl,
            bgImage = input.bgImage,
            gameSeriesCount = input.gameSeriesCount
        )
    }

}