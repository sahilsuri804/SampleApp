package com.example.sampleapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.sampleapp.data.database.AppDatabase
import com.example.sampleapp.data.models.ResultImage
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ApiDataBaseTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase


    @Before
    fun createDb() {
        db =  Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java).build()
    }

    @After
    fun stopService() {
        db.close()
    }

    @Test
    fun insertAndRead() {

        val images = arrayListOf(ResultImage("id1", "title1","25/02/2020","5","image1", 10L, 5,2),
                ResultImage("id2", "title2","25/02/2020","6","image2", 8L, 5, 3))
        db.resultDao().insertAll(*images.toTypedArray())
        val imageList = db.resultDao().all.value
        imageList?.forEach {
            assertThat(it.title, notNullValue())
            assertThat(it.id, notNullValue())
            assertThat(it.image, notNullValue())
        }
    }

    @Test
    fun checkDelete() {
        val images = arrayListOf(ResultImage("id1", "title1","25/02/2020","5","image1", 10L, 5,2),
            ResultImage("id2", "title2","25/02/2020","6","image2", 8L, 5, 3))
        db.resultDao().insertAll(*images.toTypedArray())
        val expected = 2
        assert(db.resultDao().deleteAll.equals(expected))
    }

}