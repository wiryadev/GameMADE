package com.wiryadev.gamemade.favorite

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wiryadev.gamemade.core.domain.usecase.GameUseCase

class LibraryViewModel @ViewModelInject constructor(useCase: GameUseCase) : ViewModel() {

    val data = useCase.getGameLibraries().asLiveData(viewModelScope.coroutineContext)

}