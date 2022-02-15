package com.ainrom.lbcremix.db

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.ainrom.lbcremix.data.local.album.AlbumDao
import com.ainrom.lbcremix.data.local.db.LbcRemixDatabase
import com.ainrom.lbcremix.utils.fakeAlbums
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.io.IOException

class DatabaseTest {

    private lateinit var dao: AlbumDao
    private lateinit var db: LbcRemixDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            context, LbcRemixDatabase::class.java).build()
        dao = db.albumDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /**
     * Not ideal to test all at once but had to hurry up!
     */
    @Test
    @Throws(Exception::class)
    fun insertAndLoadAndDelete() = runBlocking {
        dao.insertAlbums(fakeAlbums())

        val readAlbum = dao.getAlbum(4)
        assert(readAlbum != null)
        assert(readAlbum?.id == 4L)
        assert(readAlbum?.category == 2)
        assert(readAlbum?.title == "title ${4}")
        assert(readAlbum?.cover == "cover ${4}")
        assert(readAlbum?.thumbnail == "thumbnail ${4}")

        dao.deleteAlbum(4)
        val deletedAlbum = dao.getAlbum(4)
        assertNull(deletedAlbum)
    }
}