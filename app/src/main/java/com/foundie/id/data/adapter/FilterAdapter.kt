package com.foundie.id.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.foundie.id.data.local.response.DataFilterProduct
import com.foundie.id.databinding.ItemCatalogBinding
import com.foundie.id.settings.loadImageWithCacheBusting

class FilterAdapter : RecyclerView.Adapter<FilterAdapter.ListViewHolder>() {

    private val listFilter = ArrayList<DataFilterProduct>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(data: List<DataFilterProduct>) {
        val diffCallback = DiffUtilCallback(listFilter, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listFilter.clear()
        listFilter.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemCatalogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentFilter = listFilter[position]

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(currentFilter)
        }

        holder.bind(currentFilter)
    }

    override fun getItemCount(): Int {
        return listFilter.size
    }

    class ListViewHolder(private val binding: ItemCatalogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(product: DataFilterProduct) {
            binding.apply {
                tvProductName.text = product.productTitle
                tvProductCategory.text = product.type
                tvProductSeasonOne.text = product.season1Name
                tvProductSeasonTwo.text = product.season2Name
                ivProduct.loadImageWithCacheBusting(product.image)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataFilterProduct)
    }

    class DiffUtilCallback(
        private val oldList: List<DataFilterProduct>,
        private val newList: List<DataFilterProduct>
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
