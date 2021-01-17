package com.wiryadev.gamemade.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wiryadev.gamemade.core.domain.usecase.GameUseCase

class HomeViewModel @ViewModelInject constructor(useCase: GameUseCase) : ViewModel() {

    val data =  useCase.getGameList().asLiveData(viewModelScope.coroutineContext)

}