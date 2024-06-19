package com.foundie.id.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foundie.id.data.local.response.ImageDataResponse
import com.foundie.id.databinding.ItemSlideCameraBinding
import com.foundie.id.settings.loadImageWithCacheBusting

class ImageSliderAdapterColorCamera(private val items: List<ImageDataResponse>) :
    RecyclerView.Adapter<ImageSliderAdapterColorCamera.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: ItemSlideCameraBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ImageDataResponse) {
            with(binding) {
                ivSliderCamera.loadImageWithCacheBusting(data.imageUrl)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder {
        val binding = ItemSlideCameraBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

