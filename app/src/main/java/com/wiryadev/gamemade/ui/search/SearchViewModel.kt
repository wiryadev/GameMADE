package com.wiryadev.gamemade.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.wiryadev.gamemade.core.data.Resource
import com.wiryadev.gamemade.core.domain.usecase.GameUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlin.coroutines.coroutineContext

@FlowPreview
@ExperimentalCoroutinesApi
class SearchViewModel @ViewModelInject constructor(private val useCase: GameUseCase) : ViewModel() {

    var debounceDuration = 0

    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    val searchResult = queryChannel.asFlow()
        .debounce(debounceDuration.toLong())
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .map {
            useCase.searchGame(it)
        }
        .asLiveData(viewModelScope.coroutineContext)

    fun setDebounceDuration(needDebounce: Boolean) {
        debounceDuration = if (needDebounce) 300 else 0
    }

}