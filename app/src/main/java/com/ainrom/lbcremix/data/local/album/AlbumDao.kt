package com.ainrom.lbcremix.data.local.album

import androidx.room.*
import com.ainrom.lbcremix.model.Album
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Query("SELECT * FROM albums ORDER BY id")
    fun getAlbums(): Flow<List<Album>>

    @Query("SELECT * FROM albums WHERE category = :category ORDER BY id")
    fun getCategory(category: Int): Flow<List<Album>>

    @Query("SELECT * FROM albums WHERE id = :id")
    suspend fun getAlbum(id: Long): Album?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<Album>?)

    @Query("DELETE FROM albums WHERE id = :id")
    suspend fun deleteAlbum(id: Long)
}