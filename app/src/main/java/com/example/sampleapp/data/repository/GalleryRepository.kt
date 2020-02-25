package com.example.sampleapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sampleapp.AppExecutors
import com.example.sampleapp.data.database.AppDatabase
import com.example.sampleapp.data.models.Resource
import com.example.sampleapp.data.models.ResultImage
import com.example.sampleapp.data.network.NetworkRepository
import javax.inject.Inject

class GalleryRepository @Inject constructor(
    val networkRepository: NetworkRepository,
    val db: AppDatabase,
    val appExecutors: AppExecutors
) {

    private val TAG = GalleryRepository::class.qualifiedName
    private var liveData:MutableLiveData<Resource<List<ResultImage>>> = MutableLiveData()


    /**
     *  This function uses NetworkBoundResource, which tries
     *  to return the data from db first, and then checks for
     *  server and update the data
     */

    fun loadImages(title:String): LiveData<Resource<List<ResultImage>>> {
        return object : NetworkBoundResource<List<ResultImage>, List<ResultImage>>(appExecutors) {
            override fun saveCallResult(item: List<ResultImage>) {
                db.resultDao().deleteAll
                db.resultDao().insertAll(*item.toTypedArray())

            }

            override fun shouldFetch(data: List<ResultImage>?): Boolean {
                // For the demo App, fetching everytime from n/w
                //return data == null || data.isEmpty()
                return true
            }

            override fun loadFromDb(): LiveData<List<ResultImage>> {
                return db.resultDao().all
            }

            override fun createCall(): LiveData<Resource<List<ResultImage>>> {
                return networkRepository.getImages(title)
            }

        }.asLiveData()
    }

    /**
     * Clear the entries from DataBase and sends clearData Event
     */

    fun clearImages(): LiveData<Resource<List<ResultImage>>> {
        appExecutors.diskIO().execute {
            db.resultDao().deleteAll
        }
        liveData.value = Resource.error("cleared Data", ArrayList())
        return liveData
    }

}