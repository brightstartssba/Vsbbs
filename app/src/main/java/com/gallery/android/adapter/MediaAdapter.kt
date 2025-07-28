package com.gallery.android.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gallery.android.databinding.ItemMediaBinding
import com.gallery.android.model.MediaItem
import com.gallery.android.model.MediaType
import java.util.concurrent.TimeUnit

class MediaAdapter(
    private val onItemClick: (MediaItem) -> Unit
) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    private var allMedia = listOf<MediaItem>()
    private var filteredMedia = listOf<MediaItem>()
    private var currentFilter = MediaType.ALL

    fun updateMedia(media: List<MediaItem>) {
        allMedia = media
        filterByType(currentFilter)
    }

    fun filterByType(type: MediaType) {
        currentFilter = type
        filteredMedia = when (type) {
            MediaType.ALL -> allMedia
            MediaType.IMAGE -> allMedia.filter { it.type == MediaType.IMAGE }
            MediaType.VIDEO -> allMedia.filter { it.type == MediaType.VIDEO }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemMediaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(filteredMedia[position])
    }

    override fun getItemCount(): Int = filteredMedia.size

    inner class MediaViewHolder(
        private val binding: ItemMediaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(mediaItem: MediaItem) {
            // Load thumbnail
            Glide.with(binding.root.context)
                .load(Uri.parse(mediaItem.path))
                .centerCrop()
                .into(binding.imageView)

            // Show video-specific UI elements
            if (mediaItem.type == MediaType.VIDEO) {
                binding.playIcon.visibility = View.VISIBLE
                binding.durationText.visibility = View.VISIBLE
                binding.durationText.text = formatDuration(mediaItem.duration)
            } else {
                binding.playIcon.visibility = View.GONE
                binding.durationText.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                onItemClick(mediaItem)
            }
        }

        private fun formatDuration(durationMs: Long): String {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60
            return String.format("%d:%02d", minutes, seconds)
        }
    }
}