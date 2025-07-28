package com.gallery.android.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.gallery.android.databinding.ItemPhotoIosBinding
import com.gallery.android.model.MediaItem
import com.gallery.android.model.MediaType
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class IOSMediaAdapter(
    private val onItemClick: (MediaItem) -> Unit
) : RecyclerView.Adapter<IOSMediaAdapter.IOSMediaViewHolder>() {

    private var mediaItems = listOf<MediaItem>()
    private var selectedItems = mutableSetOf<MediaItem>()
    private var isSelectionMode = false

    fun updateMedia(media: List<MediaItem>) {
        mediaItems = media
        notifyDataSetChanged()
    }

    fun setSelectionMode(enabled: Boolean) {
        isSelectionMode = enabled
        if (!enabled) {
            selectedItems.clear()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IOSMediaViewHolder {
        val binding = ItemPhotoIosBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IOSMediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IOSMediaViewHolder, position: Int) {
        holder.bind(mediaItems[position])
    }

    override fun getItemCount(): Int = mediaItems.size

    inner class IOSMediaViewHolder(
        private val binding: ItemPhotoIosBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(mediaItem: MediaItem) {
            // Create iOS-style variable sizes for a more dynamic layout
            val layoutParams = binding.root.layoutParams as StaggeredGridLayoutManager.LayoutParams
            
            // Make some items span 2 columns randomly for visual variety
            val shouldSpanTwoColumns = position % 7 == 0 || position % 11 == 0
            layoutParams.isFullSpan = shouldSpanTwoColumns

            // Load image/video thumbnail
            Glide.with(binding.root.context)
                .load(Uri.parse(mediaItem.path))
                .centerCrop()
                .into(binding.imageView)

            // Handle video-specific UI
            if (mediaItem.type == MediaType.VIDEO) {
                binding.videoIndicator.visibility = View.VISIBLE
                binding.durationText.visibility = View.VISIBLE
                binding.durationText.text = formatDuration(mediaItem.duration)
            } else {
                binding.videoIndicator.visibility = View.GONE
                binding.durationText.visibility = View.GONE
            }

            // Handle selection mode
            val isSelected = selectedItems.contains(mediaItem)
            if (isSelectionMode) {
                binding.selectionOverlay.visibility = if (isSelected) View.VISIBLE else View.GONE
                binding.selectionCheckmark.visibility = if (isSelected) View.VISIBLE else View.GONE
            } else {
                binding.selectionOverlay.visibility = View.GONE
                binding.selectionCheckmark.visibility = View.GONE
            }

            // Click handling
            binding.root.setOnClickListener {
                if (isSelectionMode) {
                    if (isSelected) {
                        selectedItems.remove(mediaItem)
                    } else {
                        selectedItems.add(mediaItem)
                    }
                    notifyItemChanged(position)
                } else {
                    onItemClick(mediaItem)
                }
            }

            // Long press to enter selection mode
            binding.root.setOnLongClickListener {
                if (!isSelectionMode) {
                    setSelectionMode(true)
                    selectedItems.add(mediaItem)
                    notifyItemChanged(position)
                }
                true
            }
        }

        private fun formatDuration(durationMs: Long): String {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60
            return String.format("%d:%02d", minutes, seconds)
        }
    }
}