package com.wiryadev.gamemade.di

import com.wiryadev.gamemade.core.domain.usecase.GameInteractor
import com.wiryadev.gamemade.core.domain.usecase.GameUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun provideUseCase(interactor: GameInteractor): GameUseCase

}