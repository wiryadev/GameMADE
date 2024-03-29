package com.wiryadev.gamemade.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wiryadev.gamemade.core.data.Resource
import com.wiryadev.gamemade.core.domain.model.Game
import com.wiryadev.gamemade.core.domain.usecase.GameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val useCase: GameUseCase) : ViewModel() {

    lateinit var detail: LiveData<Resource<Game>>

    fun getDetail(id: Int) = viewModelScope.launch {
        if (!::detail.isInitialized) {
            detail = useCase.getDetailGame(id)
                .onStart {
                    emit(Resource.Loading())
                }
                .catch { exception -> Resource.Error(exception.toString(), null) }
                .asLiveData()
        }
    }

    fun checkFavorite(id: Int) = useCase.checkFavorite(id).asLiveData()

    fun insertGameToLibrary(game: Game) = viewModelScope.launch {
        useCase.insertGameToLibrary(game)
    }

    fun deleteGameFromLibrary(game: Game) = viewModelScope.launch {
        useCase.deleteGameFromLibrary(game)
    }

}