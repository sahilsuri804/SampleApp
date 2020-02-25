package com.example.sampleapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sampleapp.data.models.ResultImage

@Dao
interface ResultDao {

    @get:Query("SELECT * FROM ResultImage")
    val all: LiveData<List<ResultImage>>

    @Insert
    fun insertAll(vararg images: ResultImage)

    @get:Query("DELETE FROM ResultImage")
    val deleteAll: Int

}