package com.ainrom.lbcremix.data

import com.ainrom.lbcremix.data.local.album.AlbumDao
import com.ainrom.lbcremix.model.Album
import com.ainrom.lbcremix.data.remote.WebService
import com.ainrom.lbcremix.data.remote.networkBoundResource
import com.ainrom.lbcremix.di.IoDispatcher
import com.ainrom.lbcremix.model.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val albumDao: AlbumDao, // if multiple local data sources I would have added a `LocalDataSource` class
    private val webService: WebService, // if multiple remote data sources I would have added a `RemoteDataSource` class
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : Repository {

    override suspend fun albums() = networkBoundResource(
        query = { albumDao.getAlbums() },
        fetch = { webService.fetchAlbums() },
        saveFetchResult = { albums -> albumDao.insertAlbums(albums) },
        shouldFetch = { data -> data.isEmpty() }
    )

    override suspend fun albumsByCategory(filter: Int) = networkBoundResource(
        query = { albumDao.getCategory(filter) },
        fetch = { webService.fetchAlbums() },
        saveFetchResult = { albums -> albumDao.insertAlbums(albums) },
        shouldFetch = { data -> data.isEmpty() }
    )

    override suspend fun refresh(): Resource<Boolean> {
        return try {
            val refreshedAlbums = withContext(dispatcher) { webService.fetchAlbums() }
            withContext(dispatcher) { albumDao.insertAlbums(refreshedAlbums) }
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e, false)
        }
    }

    override suspend fun deleteAlbum(id: Long) = withContext(dispatcher) {
        albumDao.deleteAlbum(id)
    }

    override suspend fun getAlbum(id: Long): Album? = withContext(dispatcher) {
        albumDao.getAlbum(id)
    }
}

interface Repository {
    /**
     * Get the list of albums.
     */
    suspend fun albums(): Flow<Resource<List<Album>>>

    /**
     * Get albums from a specific category.
     */
    suspend fun albumsByCategory(filter: Int): Flow<Resource<List<Album>>>

    /**
     * Refresh the list of albums stored in the database.
     */
    suspend fun refresh(): Resource<Boolean>

    /**
     * Delete a specific album.
     */
    suspend fun deleteAlbum(id: Long)

    /**
     * Get a specific album.
     */
    suspend fun getAlbum(id: Long): Album?
}