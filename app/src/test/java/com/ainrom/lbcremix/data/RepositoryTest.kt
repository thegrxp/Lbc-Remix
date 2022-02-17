package com.ainrom.lbcremix.data

import com.ainrom.lbcremix.data.local.album.AlbumDao
import com.ainrom.lbcremix.data.remote.WebService
import com.ainrom.lbcremix.data.remote.networkBoundResource
import com.ainrom.lbcremix.model.Album
import com.ainrom.lbcremix.model.Resource
import com.ainrom.lbcremix.utils.CoroutineTestRule
import com.ainrom.lbcremix.utils.FakeData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.lang.Exception
import java.lang.RuntimeException
import java.util.concurrent.atomic.AtomicBoolean

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RepositoryTest {
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private lateinit var repo: RepositoryImpl
    private lateinit var nbr: Flow<Resource<List<Album>>>
    private lateinit var handleFetchResult: (Any) -> Unit
    private lateinit var handleShouldFetch: (List<Album>?) -> Boolean
    private lateinit var handleFetch: () -> Any
    private val fetchedOnce = AtomicBoolean(false)

    @Mock
    private lateinit var webService: WebService

    @Mock
    private lateinit var albumDao: AlbumDao

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repo = RepositoryImpl(
            albumDao,
            webService,
            coroutinesTestRule.testDispatcher
        )

        nbr = networkBoundResource(
            query = { albumDao.getAlbums() },
            fetch = { handleFetch },
            shouldFetch = { handleShouldFetch(it) && fetchedOnce.compareAndSet(false, true) },
            saveFetchResult = { handleFetchResult(it) }
        )
    }

    @OptIn(FlowPreview::class)
    @Test
    fun albums_returnsAlbums() = runTest {
        `when`(albumDao.getAlbums()).thenReturn(FakeData.albums1.asFlow())

        val outputFlow = repo.albums().toList()

        assert(outputFlow.count() == 2)
        assert(outputFlow[0] is Resource.Loading)
        assert(outputFlow[1] is Resource.Success)
        assert(outputFlow[1].data?.size == 10)
    }

    @OptIn(FlowPreview::class)
    @Test
    fun albumsByCategory_returnsAlbums() = runTest {
        `when`(albumDao.getCategory(2)).thenReturn(FakeData.albums2.asFlow())

        val outputFlow = repo.albumsByCategory(2).toList()

        assert(outputFlow.count() == 2)
        assert(outputFlow[0] is Resource.Loading)
        assert(outputFlow[1] is Resource.Success)
        assert(outputFlow[1].data?.size == 5)
    }

    /**
     * Could / should be in a NetworkBoundResourceTest class
     */
    @OptIn(FlowPreview::class)
    @Test
    fun albums_dbQuery_returnsAlbums() = runTest {
        `when`(albumDao.getAlbums()).thenReturn(FakeData.albums1.asFlow())
        handleShouldFetch = { false }

        val outputFlow = nbr.toList()

        assert(outputFlow.count() == 2)
        assert(outputFlow[0] is Resource.Loading)
        assert(outputFlow[1] is Resource.Success)
        assert(outputFlow[1].data?.size == 10)
    }

    /**
     * Could / should be in a NetworkBoundResourceTest class
     */
    @OptIn(FlowPreview::class)
    @Test
    fun albums_networkCall_returnsAlbums() = runTest {
        `when`(albumDao.getAlbums()).thenReturn(FakeData.albums1.asFlow())
        handleShouldFetch = { true }
        handleFetch = { true }
        handleFetchResult = {
            `when`(albumDao.getAlbums()).thenReturn(FakeData.albums2.asFlow())
        }

        val outputFlow = nbr.toList()

        assert(outputFlow.count() == 3)
        assert(outputFlow[0] is Resource.Loading)
        assert(outputFlow[1] is Resource.Loading)
        assert(outputFlow[1].data?.size == 10)
        assert(outputFlow[2] is Resource.Success)
        assert(outputFlow[2].data?.size == 5)
    }

    /**
     * Could / should be in a NetworkBoundResourceTest class
     */
    @OptIn(FlowPreview::class)
    @Test
    fun albums_networkCall_throwException() = runTest {
        `when`(albumDao.getAlbums()).thenReturn(FakeData.albums1.asFlow())
        handleShouldFetch = { true }
        handleFetchResult = { throw Exception() }

        val outputFlow = nbr.toList()

        assert(outputFlow.count() == 3)
        assert(outputFlow[0] is Resource.Loading)
        assert(outputFlow[1] is Resource.Loading)
        assert(outputFlow[2] is Resource.Error)
    }

    @Test
    fun refresh_networkException_returnsError() = runTest {
        `when`(webService.fetchAlbums()).thenThrow(RuntimeException())
        val output = repo.refresh()
        assert(output is Resource.Error)
    }

    @Test
    fun refresh_insertException_returnsError() = runTest {
        `when`(albumDao.insertAlbums(null))
        val output = repo.refresh()
        assert(output is Resource.Error)
    }

    @Test
    fun refresh_returnsSuccess() = runTest {
        `when`(webService.fetchAlbums()).thenReturn(FakeData.albums1.invoke())
        val output = repo.refresh()
        assert(output is Resource.Success)
    }

    @Test
    fun getAlbum_existingId_returnsAlbum() = runTest {
        `when`(albumDao.getAlbum(999)).thenReturn(FakeData.album)
        val firstItem = repo.getAlbum(999)
        assert(firstItem?.id == 999L)
    }

    @Test
    fun getAlbum_unknownId_returnsNull() = runTest {
        val firstItem = repo.getAlbum(111)
        assert(firstItem == null)
    }
}