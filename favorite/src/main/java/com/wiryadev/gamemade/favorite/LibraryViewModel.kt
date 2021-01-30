package com.wiryadev.gamemade.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wiryadev.gamemade.core.domain.usecase.GameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(useCase: GameUseCase) : ViewModel() {

    val data = useCase.getGameLibraries().asLiveData(viewModelScope.coroutineContext)

}