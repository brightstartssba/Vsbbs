package com.gallery.android.model

data class MediaItem(
    val id: Long,
    val path: String,
    val name: String,
    val type: MediaType,
    val size: Long,
    val dateAdded: Long,
    val duration: Long = 0 // For videos only, in milliseconds
)

enum class MediaType {
    IMAGE, VIDEO, ALL
}