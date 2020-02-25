package com.example.sampleapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleapp.R
import com.example.sampleapp.data.models.ResultImage
import com.example.sampleapp.databinding.ItemMainBinding


class GalleryAdapter : RecyclerView.Adapter<GalleryAdapter.ViewHolder>(), Filterable {

    private var imageList = mutableListOf<ResultImage>()
    private var unFilteredImageList = mutableListOf<ResultImage>()

    class ViewHolder(val itemMainBinding: ItemMainBinding) : RecyclerView.ViewHolder(itemMainBinding.getRoot())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemMainBinding: ItemMainBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.item_main, parent, false)
        return ViewHolder(itemMainBinding)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun updateList(images: List<ResultImage>, isFilter:Boolean) {
        unFilteredImageList = images as MutableList<ResultImage>
        if(!isFilter) {
            imageList = unFilteredImageList
            notifyDataSetChanged()
        }
        else this.filter.filter("filter")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemMainBinding.resultImage = imageList.get(position)
        holder.itemMainBinding.executePendingBindings()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            var filteredImageList = mutableListOf<ResultImage>()
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charSequenceString = constraint.toString()
                filteredImageList = if (charSequenceString.isEmpty()) {
                    unFilteredImageList
                } else {
                    for (image in unFilteredImageList) {
                        image.apply {
                            if((points + score + topic_id) %2L ==0L)
                                filteredImageList.add(this)
                        }
                    }
                    filteredImageList
                }
                val results = FilterResults()
                results.values = filteredImageList
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                imageList = (results.values as List<ResultImage>).toMutableList()
                notifyDataSetChanged()
            }
        }
    }
}