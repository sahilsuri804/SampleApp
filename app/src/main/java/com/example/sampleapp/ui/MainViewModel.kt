package com.example.sampleapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sampleapp.data.models.Resource
import com.example.sampleapp.data.models.ResultImage
import com.example.sampleapp.data.repository.GalleryRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(val galleryRepo: GalleryRepository) : ViewModel() {

    private val TAG = MainViewModel::class.qualifiedName


    private val loadTrigger = MutableLiveData<String>()
    var isFilterEnabled:Boolean = false
    private val filterChecked = MutableLiveData<Boolean>()

    // reload true means reload from n/w, false means clear everything
    var listImage: LiveData<Resource<List<ResultImage>>> = Transformations.switchMap(loadTrigger) { reload ->
        if(!reload.toLowerCase().equals("clear")) {
            galleryRepo.loadImages(reload)
        } else {
            galleryRepo.clearImages()
        }
    }

    /**
     * This gets called from toolbar menu
     */
    fun clearImages() {
        loadTrigger.value = "clear"
    }

    fun setQuery(query: String) {
        loadTrigger.value = query
    }

}