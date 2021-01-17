package com.wiryadev.gamemade.di

import com.wiryadev.gamemade.core.domain.usecase.GameUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface MapsModuleDependencies {

    fun useCase(): GameUseCase

}