package com.foundie.id.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.foundie.id.data.local.response.DataPostItem
import com.foundie.id.databinding.ItemCommunityTweetBinding
import com.foundie.id.settings.getTimeAgo
import com.foundie.id.settings.loadImageWithCacheBusting
import java.text.SimpleDateFormat
import java.util.*

class UserPostAdapter : RecyclerView.Adapter<UserPostAdapter.ListViewHolder>() {

    private val listPostUser = ArrayList<DataPostItem>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(data: List<DataPostItem>) {
        val diffCallback = DiffUtilCallback(listPostUser, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listPostUser.clear()
        listPostUser.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemCommunityTweetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding, onItemClickCallback)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listPostUser[position])
    }

    override fun getItemCount(): Int {
        return listPostUser.size
    }

    inner class ListViewHolder(private val binding: ItemCommunityTweetBinding, private val onItemClickCallback: OnItemClickCallback) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(postuser: DataPostItem) {
            binding.apply {
                ivCommunity.visibility = View.GONE
                tvCommunityName.visibility = View.GONE
                tvUsername.text = postuser.name
                tvPostDescription.text = postuser.title

                val dateTime = postuser.timestamp
                val dateTimeMillis = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).parse(dateTime)!!.time
                tvTimestamp.text = getTimeAgo(dateTimeMillis)

                imgUser.loadImageWithCacheBusting(postuser.profileImageUrl)
                ivPostImage.loadImageWithCacheBusting(postuser.imageUrls[0])

                imgUser.setOnClickListener {
                    onItemClickCallback.onProfileImageClicked(postuser)
                }

                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(postuser)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataPostItem)
        fun onProfileImageClicked(data: DataPostItem)
    }

    class DiffUtilCallback(
        private val oldList: List<DataPostItem>,
        private val newList: List<DataPostItem>
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
