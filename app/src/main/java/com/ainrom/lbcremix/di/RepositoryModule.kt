package com.ainrom.lbcremix.di

import com.ainrom.lbcremix.data.Repository
import com.ainrom.lbcremix.data.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindRepository(repositoryImpl: RepositoryImpl): Repository
}
