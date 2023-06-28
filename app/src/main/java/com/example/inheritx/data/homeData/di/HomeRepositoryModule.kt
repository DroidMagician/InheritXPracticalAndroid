package com.example.inheritx.data.homeData.di

import com.example.inheritx.data.homeData.repository.HomeRepositoryImpl
import com.example.inheritx.domain.homeData.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeRepositoryModule {

    @Binds
    @Singleton
    internal abstract fun bindCharsRepository(repository: HomeRepositoryImpl): HomeRepository
}