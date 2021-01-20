package com.wiryadev.gamemade.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wiryadev.gamemade.core.domain.usecase.GameUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest

@FlowPreview
@ExperimentalCoroutinesApi
class SearchViewModel @ViewModelInject constructor(private val useCase: GameUseCase) : ViewModel() {

    private var debounceDuration = 0

    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    val searchResult = queryChannel.asFlow()
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