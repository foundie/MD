package com.foundie.id.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foundie.id.data.local.response.ImageDataResponse
import com.foundie.id.data.local.response.ImageDataResponseColor
import com.foundie.id.databinding.ItemSlideCameraBinding
import com.foundie.id.databinding.ItemSliderBinding
import com.foundie.id.settings.loadImageWithCacheBusting

class ImageSliderAdapterColorCamera(private val list: List<ImageDataResponseColor>) : RecyclerView.Adapter<ImageSliderAdapterColorCamera.ViewHolder>() {

    inner class ViewHolder(val binding: ItemSliderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageData: ImageDataResponseColor) {
            binding.colorView.setBackgroundColor(imageData.color)
            binding.colorView.visibility = View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}