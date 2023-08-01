package com.example.thecodingstory.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thecodingstory.databinding.ItemStoryBinding
import com.example.thecodingstory.api.response.ListStoryItem
import com.example.thecodingstory.ui.story.StoryDetailActivity

class StoriesAdapter :
    PagingDataAdapter<ListStoryItem, StoriesAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding =  ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data,holder)
        }
    }

    class MyViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem, holder: MyViewHolder) {
            binding.judulStory.text = data.name
            binding.timeStory.text = data.createdAt

            Glide.with(holder.itemView.context)
                .load(data.photoUrl)
                .into(binding.imgStory)

            holder.itemView.setOnClickListener {
                val intentDetail = Intent(holder.itemView.context, StoryDetailActivity::class.java)
                intentDetail.putExtra("id",data.id)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        holder.itemView.context as Activity,
                        Pair(binding.imgStory,"photo"),
                        Pair(binding.judulStory,"username"),
                    )

                holder.itemView.context.startActivity(intentDetail, optionsCompat.toBundle())

            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
