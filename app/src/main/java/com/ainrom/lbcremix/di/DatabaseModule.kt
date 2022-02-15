package com.ainrom.lbcremix.di

import android.app.Application
import androidx.room.Room
import com.ainrom.lbcremix.data.local.album.AlbumDao
import com.ainrom.lbcremix.data.local.db.LbcRemixDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): LbcRemixDatabase {
        return Room
            .databaseBuilder(app, LbcRemixDatabase::class.java, "lbcremix.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideAlbumDao(db: LbcRemixDatabase): AlbumDao {
        return db.albumDao()
    }
}