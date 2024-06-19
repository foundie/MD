package com.foundie.id.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.foundie.id.data.local.response.GroupsDataItem
import com.foundie.id.databinding.ItemCommunityBinding
import com.foundie.id.settings.loadImageWithCacheBusting

class GroupCommunityAdapter : RecyclerView.Adapter<GroupCommunityAdapter.ListViewHolder>() {

    private val listGroup = ArrayList<GroupsDataItem>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(data: List<GroupsDataItem>) {
        val diffCallback = DiffUtilCallback(listGroup, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listGroup.clear()
        listGroup.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemCommunityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentGroup = listGroup[position]

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(currentGroup)
        }

        holder.bind(currentGroup)
    }

    override fun getItemCount(): Int {
        return listGroup.size
    }

    class ListViewHolder(private val binding: ItemCommunityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(group: GroupsDataItem) {
            binding.apply {
                tvCommunityName.text = group.title
                tvTotalMembers.text = group.subscription.toString()
                ivUser.loadImageWithCacheBusting(group.profileImageUrl)
                ivCommunity.loadImageWithCacheBusting(group.coverImageUrl)
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
        fun onItemClicked(data: GroupsDataItem)
    }

    class DiffUtilCallback(
        private val oldList: List<GroupsDataItem>,
        private val newList: List<GroupsDataItem>
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
