package com.wiryadev.gamemade.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.wiryadev.gamemade.core.domain.usecase.GameUseCase

class SearchViewModel @ViewModelInject constructor(private val useCase: GameUseCase) : ViewModel() {

    private val searchKeyword = MutableLiveData<String>()

    val users = Transformations.switchMap(searchKeyword) {
        searchKeyword.value?.let {
            useCase.searchGame(it).asLiveData(viewModelScope.coroutineContext)
        }
    }

    fun searchGame(search: String) {
        searchKeyword.value = search
    }

}