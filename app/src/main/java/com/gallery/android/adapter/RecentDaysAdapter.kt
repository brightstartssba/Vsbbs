package com.gallery.android.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gallery.android.databinding.ItemRecentDayBinding
import com.gallery.android.model.MediaItem

class RecentDaysAdapter(
    private val onItemClick: (List<MediaItem>) -> Unit
) : RecyclerView.Adapter<RecentDaysAdapter.RecentDayViewHolder>() {

    private var recentDaysData = listOf<Pair<String, List<MediaItem>>>()

    fun updateData(data: List<Pair<String, List<MediaItem>>>) {
        recentDaysData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentDayViewHolder {
        val binding = ItemRecentDayBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecentDayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentDayViewHolder, position: Int) {
        holder.bind(recentDaysData[position])
    }

    override fun getItemCount(): Int = recentDaysData.size

    inner class RecentDayViewHolder(
        private val binding: ItemRecentDayBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dayData: Pair<String, List<MediaItem>>) {
            val (dateLabel, mediaItems) = dayData
            
            // Set date label
            binding.dateLabel.text = dateLabel
            
            // Load the first/most recent image as thumbnail
            if (mediaItems.isNotEmpty()) {
                val thumbnailItem = mediaItems.first()
                Glide.with(binding.root.context)
                    .load(Uri.parse(thumbnailItem.path))
                    .centerCrop()
                    .into(binding.imageView)
            }

            // Click handling
            binding.root.setOnClickListener {
                onItemClick(mediaItems)
            }
        }
    }
}