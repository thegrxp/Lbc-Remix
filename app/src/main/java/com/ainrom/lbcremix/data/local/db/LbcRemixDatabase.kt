package com.ainrom.lbcremix.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ainrom.lbcremix.data.local.album.AlbumDao
import com.ainrom.lbcremix.model.Album

@Database(
    entities = [(Album::class)],
    version = 1,
    exportSchema = false
)

abstract class LbcRemixDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}