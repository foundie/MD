package com.foundie.id.data.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.core.util.Pair
import com.bumptech.glide.Glide
import com.foundie.id.data.local.response.ProductData
import com.foundie.id.databinding.ItemCatalogBinding
import com.foundie.id.settings.loadImageWithCacheBusting

class CatalogAdapter : RecyclerView.Adapter<CatalogAdapter.ListViewHolder>() {

    private val listCatalog = ArrayList<ProductData>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(data: List<ProductData>) {
        val diffCallback = DiffUtilCallback(listCatalog, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listCatalog.clear()
        listCatalog.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemCatalogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentCatalog = listCatalog[position]

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(currentCatalog)
        }

        holder.bind(currentCatalog)
    }

    override fun getItemCount(): Int {
        return listCatalog.size
    }

    class ListViewHolder(private val binding: ItemCatalogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(product: ProductData) {
            binding.apply {
                tvProductName.text = product.productTitle
                tvProductCategory.text = product.type
                tvProductSeasonOne.text = product.season1Name
                tvProductSeasonTwo.text = product.season2Name
                ivProduct.loadImageWithCacheBusting(product.image)
            }

            itemView.setOnClickListener {
                //val intent = Intent(itemView.context, StoryDetailActivity::class.java)
                //intent.putExtra(StoryDetailActivity.EXTRA_DETAIL_STORY, product)

//                val optionsCompat: ActivityOptionsCompat =
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        itemView.context as Activity,
////                        Pair(binding.imgItemPhoto, "image"),
//                    )
//                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ProductData)
    }

    class DiffUtilCallback(
        private val oldList: List<ProductData>,
        private val newList: List<ProductData>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }
    }
}
