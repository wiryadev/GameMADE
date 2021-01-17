package com.wiryadev.gamemade.core.di

import com.wiryadev.gamemade.core.data.GameRepository
import com.wiryadev.gamemade.core.domain.repository.IGameRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [NetworkModule::class, DatabaseModule::class]
)
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(repository: GameRepository): IGameRepository

}