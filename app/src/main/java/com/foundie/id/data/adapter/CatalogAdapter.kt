package com.foundie.id.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foundie.id.data.local.response.ProductData
import com.foundie.id.databinding.ItemCatalogBinding

class CatalogAdapter : ListAdapter<ProductData, CatalogAdapter.CatalogViewHolder>(ProductDataDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        val binding = ItemCatalogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        val currentProduct = getItem(position)
        holder.bind(currentProduct)
    }

    inner class CatalogViewHolder(private val binding: ItemCatalogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductData) {
            binding.apply {
                tvProductName.text = product.productTitle
                tvProductCategory.text = product.type
                tvProductSeasonOne.text = product.season1Name
                tvProductSeasonTwo.text = product.season2Name

                Glide.with(itemView)
                    .load(product.image)
                    .into(ivProduct)

                itemView.setOnClickListener {
                    // Handle item click here if needed
                }
            }
        }
    }
}

class ProductDataDiffCallback : DiffUtil.ItemCallback<ProductData>() {
    override fun areItemsTheSame(oldItem: ProductData, newItem: ProductData): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ProductData, newItem: ProductData): Boolean {
        return oldItem == newItem
    }
}
