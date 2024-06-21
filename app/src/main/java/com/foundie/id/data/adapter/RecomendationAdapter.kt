package com.foundie.id.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.foundie.id.data.local.response.RecomendationDataItem
import com.foundie.id.databinding.ItemCatalogBinding
import com.foundie.id.settings.loadImageWithCacheBusting

class RecomendationAdapter : RecyclerView.Adapter<RecomendationAdapter.ListViewHolder>() {

    private val listRecomendation = ArrayList<RecomendationDataItem>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(data: List<RecomendationDataItem>) {
        val diffCallback = DiffUtilCallback(listRecomendation, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listRecomendation.clear()
        listRecomendation.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemCatalogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentRecomendation = listRecomendation[position]

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(currentRecomendation)
        }

        holder.bind(currentRecomendation)
    }

    override fun getItemCount(): Int {
        return listRecomendation.size
    }

    class ListViewHolder(private val binding: ItemCatalogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(product: RecomendationDataItem) {
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
        fun onItemClicked(data: RecomendationDataItem)
    }

    class DiffUtilCallback(
        private val oldList: List<RecomendationDataItem>,
        private val newList: List<RecomendationDataItem>
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
