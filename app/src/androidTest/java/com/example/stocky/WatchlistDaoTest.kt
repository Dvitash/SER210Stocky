package com.example.stocky

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import org.junit.runner.RunWith
import java.io.IOException
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class WatchlistDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var watchlistDao: WatchlistDao
    private lateinit var database: AppDatabase

    private val entry1 = WatchlistEntry(id = 1, symbol = "AAPL", addedAt = 111111L)
    private val entry2 = WatchlistEntry(id = 2, symbol = "GOOG", addedAt = 222222L)

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        watchlistDao = database.watchlistDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    private suspend fun insertOneEntry() {
        watchlistDao.insertEntry(entry1)
    }

    private suspend fun insertTwoEntries() {
        watchlistDao.insertEntry(entry1)
        watchlistDao.insertEntry(entry2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsEntryIntoDB() = runBlocking {
        insertOneEntry()
        val entries = watchlistDao.getAllEntries()
        assertEquals("AAPL", entries[0].symbol)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllEntries_returnsAllEntriesFromDB() = runBlocking {
        insertTwoEntries()
        val entries = watchlistDao.getAllEntries()
        assertEquals("AAPL", entries[0].symbol)
        assertEquals("GOOG", entries[1].symbol)
    }
}
