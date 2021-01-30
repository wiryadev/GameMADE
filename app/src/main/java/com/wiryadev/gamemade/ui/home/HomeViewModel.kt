package com.wiryadev.gamemade.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wiryadev.gamemade.core.domain.usecase.GameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(useCase: GameUseCase) : ViewModel() {

    val data =  useCase.getGameList().asLiveData(viewModelScope.coroutineContext)

}