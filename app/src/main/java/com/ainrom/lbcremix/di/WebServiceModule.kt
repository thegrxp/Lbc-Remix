package com.ainrom.lbcremix.di

import com.ainrom.lbcremix.data.remote.WebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class WebServiceModule {

    @Singleton
    @Provides
    fun provideWebService(): WebService {
        return Retrofit.Builder()
            .baseUrl("https://static.leboncoin.fr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WebService::class.java)
    }
}