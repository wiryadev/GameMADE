package com.wiryadev.gamemade.core.domain.usecase

import com.wiryadev.gamemade.core.domain.model.Game
import com.wiryadev.gamemade.core.domain.repository.IGameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GameInteractor @Inject constructor(private val gameRepository: IGameRepository) : GameUseCase {

    override fun getGameList() = gameRepository.getGameList()

    override suspend fun searchGame(search: String) = gameRepository.searchGame(search)

    override fun getGameLibraries(): Flow<List<Game>> = gameRepository.getGameLibraries()

    override suspend fun getDetailGame(id: Int) = gameRepository.getDetailGame(id)

    override fun checkFavorite(id: Int): Flow<Int> = gameRepository.checkFavorite(id)

    override suspend fun insertGameToLibrary(game: Game) {
        gameRepository.insertGameToLibrary(game)
    }

    override suspend fun deleteGameFromLibrary(game: Game) {
        gameRepository.deleteGameFromLibrary(game)
    }

}