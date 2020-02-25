package com.example.sampleapp.data.network

import com.example.sampleapp.data.models.ResponseGallery
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("{/{sortParam}/{time}/")
    fun getSearchResults(
        @Path("sortParam") sortParam: String,
        @Path("time") time: String,
        @Query("q") deviceId:String
    ): Call<ResponseGallery>

}