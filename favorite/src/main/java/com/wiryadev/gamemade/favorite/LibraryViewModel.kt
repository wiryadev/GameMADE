package com.wiryadev.gamemade.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wiryadev.gamemade.core.domain.model.Game
import com.wiryadev.gamemade.core.domain.usecase.GameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(private val useCase: GameUseCase) : ViewModel() {

    val pagingDataFlow: Flow<PagingData<Game>>

    init {
        pagingDataFlow = getGameLibraries().cachedIn(viewModelScope)
    }

    private fun getGameLibraries() = useCase.getGameLibraries()
}