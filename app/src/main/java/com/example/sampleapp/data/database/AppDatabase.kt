package com.example.sampleapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sampleapp.data.models.ResultImage

@Database(
    entities = [ResultImage ::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun resultDao(): ResultDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "sample.db")
            .build()
    }
}