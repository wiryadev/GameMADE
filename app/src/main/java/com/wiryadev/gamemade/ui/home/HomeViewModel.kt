package com.wiryadev.gamemade.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wiryadev.gamemade.core.data.Resource
import com.wiryadev.gamemade.core.domain.model.Game
import com.wiryadev.gamemade.core.domain.usecase.GameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: GameUseCase) : ViewModel() {

    lateinit var data: LiveData<Resource<List<Game>>>

    init {
        getGameList()
    }

    private fun getGameList() = viewModelScope.launch {
        if (!::data.isInitialized) {
            data = useCase.getGameList().asLiveData(this.coroutineContext)
        }
    }
}