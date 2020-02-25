package com.example.sampleapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sampleapp.data.repository.GalleryRepository
import com.example.sampleapp.ui.MainViewModel
import javax.inject.Inject


class MainViewModelFactory @Inject constructor (val galleryRepo: GalleryRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(galleryRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}