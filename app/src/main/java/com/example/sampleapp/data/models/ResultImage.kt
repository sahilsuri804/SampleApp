package com.example.sampleapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ResultImage(
    @PrimaryKey
    val id:String,
    val title:String,
    val dateOfPost: String,
    val NoOfImages:String,
    val image:String,
    val points: Long,
    val score: Long,
    val topic_id: Int
)