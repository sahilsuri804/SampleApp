package com.example.sampleapp.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



data class ResponseGallery(
    @SerializedName("data")
    @Expose
    var data: List<Gallery>? = null
)