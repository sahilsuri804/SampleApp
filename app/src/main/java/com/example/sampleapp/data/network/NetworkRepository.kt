package com.example.sampleapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sampleapp.data.models.*
import com.example.sampleapp.data.repository.GalleryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NetworkRepository @Inject constructor (val apiInterface: ApiInterface) {
    private val TAG = GalleryRepository::class.qualifiedName

    /**
     * Get the list of the Images from the API
     */
    fun getImages(title:String):LiveData<Resource<List<ResultImage>>> {
        // Rather than using LiveDataInterceptor, creating instance of liveData and updating it
        var liveData: MutableLiveData<Resource<List<ResultImage>>> = MutableLiveData()

        apiInterface.getSearchResults("top","week",title).enqueue(object :Callback<ResponseGallery> {
            override fun onFailure(call: Call<ResponseGallery>, t: Throwable) {
                Log.d(TAG,"On Error")
            }

            override fun onResponse(call: Call<ResponseGallery>, response: Response<ResponseGallery>) {
                Log.d(TAG,"On Response "+response.body()?.data?.size)
//                response.body()?.data?.forEach {
//                    Log.d(
//                        TAG,
//                        "cover: ${it.cover}, isAlbum ${it.is_album}, datetime= ${it.datetime}, imege count =${it.images_count} " +
//                                " images = ${it.images}, link= ${it.link}"
//                    )
//                }
                var imageList = response.body()?.data ?: arrayListOf()

                var images =
                    imageList.filter { it.is_album }
                    .sortedByDescending { it.datetime }
                    .map {
                    var dateLong = it.datetime * 1000L
                    var date = Date(dateLong);
                    var df2 =  SimpleDateFormat("dd/MM/YYYY h:mm a");
                    var dateText = df2.format(date)
                    ResultImage(it.id, it.title, dateText, "Count is ${it.images_count}", it.images.get(0).link,
                        it.points, it.score, it.topic_id)

                }
                liveData.value = Resource.success(images)

            }

        })
        return liveData
    }

}