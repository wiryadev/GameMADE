package com.wiryadev.gamemade.core.domain.usecase

import com.wiryadev.gamemade.core.data.GameRepository
import com.wiryadev.gamemade.core.data.Resource
import com.wiryadev.gamemade.core.domain.model.Game
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GameInteractor @Inject constructor(private val gameRepository: GameRepository) : GameUseCase {

    override fun getGameList() = gameRepository.getGameList()

    override fun searchGame(search: String) = gameRepository.searchGame(search)

    override fun getGameLibraries(): Flow<List<Game>> {
        TODO("Not yet implemented")
    }

    override fun getDetailGame(id: Int): Flow<Resource<Game>> {
        TODO("Not yet implemented")
    }

    override fun checkFavorite(id: Int): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun insertGameToLibrary(game: Game) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGameFromLibrary(game: Game) {
        TODO("Not yet implemented")
    }

}