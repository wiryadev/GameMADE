package com.wiryadev.gamemade.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wiryadev.gamemade.core.domain.usecase.GameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class SearchViewModel @Inject constructor(private val useCase: GameUseCase) : ViewModel() {

    private var debounceDuration = 0

    val queryChannel = MutableStateFlow("")

    val searchResult = queryChannel
        .debounce(debounceDuration.toLong())
        .filter {
            it.trim().isNotEmpty()
        }
        .mapLatest {
            useCase.searchGame(it)
        }
        .asLiveData(viewModelScope.coroutineContext)

    fun setDebounceDuration(needDebounce: Boolean) {
        debounceDuration = if (needDebounce) 300 else 0
    }

}