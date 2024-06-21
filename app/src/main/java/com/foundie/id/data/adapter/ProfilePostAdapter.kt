package com.foundie.id.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.foundie.id.data.local.response.PostsItem
import com.foundie.id.databinding.ItemCommunityTweetBinding
import com.foundie.id.settings.getTimeAgo
import com.foundie.id.settings.loadImageWithCacheBusting
import java.text.SimpleDateFormat
import java.util.*

class ProfilePostAdapter : RecyclerView.Adapter<ProfilePostAdapter.ListViewHolder>() {

    private val listPostProfile = ArrayList<PostsItem>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(data: List<PostsItem>) {
        val diffCallback = DiffUtilCallback(listPostProfile, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listPostProfile.clear()
        listPostProfile.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemCommunityTweetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listPostProfile[position])
    }

    override fun getItemCount(): Int {
        return listPostProfile.size
    }

    inner class ListViewHolder(private val binding: ItemCommunityTweetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(listPostProfile[adapterPosition])
            }
        }

        fun bind(postuser: PostsItem) {
            binding.apply {
//                ivCommunity.visibility = View.GONE
//                tvCommunityName.visibility = View.GONE
                tvUsername.text = postuser.name
                tvPostDescription.text = postuser.text

                val dateTime = postuser.timestamp
                val dateTimeMillis = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).parse(dateTime)!!.time
                tvTimestamp.text = getTimeAgo(dateTimeMillis)

                imgUser.loadImageWithCacheBusting("https://www.greenscene.co.id/wp-content/uploads/2022/01/Naruto-1-696x497.jpg")
                // ivPostImage.loadImageWithCacheBusting(postuser.imageUrls[0].toString())
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: PostsItem)
    }

    class DiffUtilCallback(
        private val oldList: List<PostsItem>,
        private val newList: List<PostsItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem// Assuming PostsItem has an id field
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }
    }
}
